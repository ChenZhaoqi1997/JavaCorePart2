package com.company.ch4.threaded;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ThreadedEchoHandler implements Runnable {
    private Socket incoming;
    public ThreadedEchoHandler(Socket incoming) {
        this.incoming = incoming;
    }
    public void run() {
        try(InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream()) {
            Scanner in = new Scanner(inStream, StandardCharsets.UTF_8);
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(outStream, StandardCharsets.UTF_8),
                    true
            );
            out.println("Hello! Enter BYE to exit.");
            boolean done = false;
            while (!done && in.hasNextLine()) {
                String line = in.nextLine();
                out.println("Echo: " + line);
                if (line.trim().equals("BYE")) {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
