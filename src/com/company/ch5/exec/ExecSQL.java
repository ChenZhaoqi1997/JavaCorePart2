package com.company.ch5.exec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class ExecSQL {
    public static void main(String[] args) {
        try(Scanner in = args.length == 0 ? new Scanner(System.in) :
                new Scanner(Paths.get(args[0]), StandardCharsets.UTF_8)) {
            try (Connection conn = getConnect();
                 Statement stat = conn.createStatement()) {
                while (true) {
                    if (args.length == 0) {
                        System.out.println("Enter commend or EXIT to exit:");
                    }
                    if (!in.hasNextLine()) {
                        return;
                    }
                    String line = in.nextLine().trim();
                    if (line.equalsIgnoreCase("EXIT")) {
                        return;
                    }
                    if (line.endsWith(";")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    try {
                        boolean isResult = stat.execute(line);
                        if (isResult) {
                            try (ResultSet rs = stat.getResultSet()) {
                                showResultSet(rs);
                            }
                        } else {
                            int updateCount = stat.getUpdateCount();
                            System.out.println(updateCount + " rows updated");
                        }
                    } catch (SQLException e) {
                        for (Throwable t : e) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnect() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("D:\\005_some_code\\Java\\IdeaProjects\\JavaCorePart2\\src\\com\\company\\ch5\\test\\database.properties"))) {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.driver");
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        return DriverManager.getConnection(url, username, password);
    }

    public static void showResultSet(ResultSet result) throws SQLException {
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i < columnCount; i++) {
            if (i > 1) {
                System.out.println(",");
            }
            System.out.print(metaData.getColumnLabel(i));
        }
        System.out.println();
        while (result.next()) {
            for (int i = 1; i < columnCount; i++) {
                if (i > 1) {
                    System.out.print(",");
                }
                System.out.print(result.getString(i));
            }
            System.out.println();
        }
    }
}
