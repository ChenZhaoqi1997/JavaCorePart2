package com.company.ch4.post;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PostTest {
    public static void main(String[] args) throws IOException {
        String propsFilename = args.length > 0 ? args[0] : "post/post.properties";
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get(propsFilename))) {
            props.load(in);
        }
        String urlString = props.remove("url").toString();
        Object userAgent = props.remove("User-Agent");
        Object redirects = props.remove("redirects");
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        String result = doPost(new URL(urlString), props,
                userAgent == null ? null : userAgent.toString(),
                redirects == null ? -1 : Integer.parseInt(redirects.toString()));
        System.out.println(result);
    }

    public static String doPost(URL url, Map<Object, Object>nameValuePairs,
                                String userAgent, int redirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        if (userAgent != null) {
            connection.setRequestProperty("User-Agent", userAgent);
        }
        if (redirects >= 0) {
            connection.setInstanceFollowRedirects(false);
        }
        connection.setDoOutput(true);
        try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
            boolean first = true;
            for (Map.Entry<Object, Object> pair : nameValuePairs.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    out.print('&');
                }
                String name = pair.getKey().toString();
                String value = pair.getValue().toString();
                out.print(name);
                out.print('=');
                out.print(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
        }
        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        if (redirects > 0) {
            int resposeCode = connection.getResponseCode();
            if (resposeCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    resposeCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                    resposeCode == HttpURLConnection.HTTP_SEE_OTHER) {
                String location = connection.getHeaderField("Location");
                if (location != null) {
                    URL base = connection.getURL();
                    connection.disconnect();
                    return doPost(new URL(base, location), nameValuePairs, userAgent, redirects - 1);
                }
            }
        } else if (redirects == 0) {
            throw new IOException("Too many redirects");
        }
        StringBuilder respone = new StringBuilder();
        try (Scanner in = new Scanner(connection.getInputStream(), encoding)) {
            while (in.hasNextLine()) {
                respone.append(in.nextLine());
                respone.append("\n");
            }
        } catch (IOException e) {
            InputStream err = connection.getErrorStream();
            if (err == null) {
                throw e;
            }
            try (Scanner in = new Scanner(err)) {
                respone.append(in.nextLine());
                respone.append("\n");
            }
        }
        return respone.toString();
    }
}
