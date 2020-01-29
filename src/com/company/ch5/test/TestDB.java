package com.company.ch5.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class TestDB {
    public static void main(String[] args) throws IOException {
        try {
            runTest();
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
    }

    public static void runTest() throws SQLException, IOException {
        try (Connection conn = getConnection();
             Statement stat = conn.createStatement()) {
            stat.executeUpdate("create table Greetings(Message char(20))");
            stat.executeUpdate("insert into Greetings values ('Hello, world!')");
            try(ResultSet result = stat.executeQuery("select * from Greetings")) {
                if (result.next()) {
                    System.out.println(result.getString(1));
                }
            }
            stat.executeUpdate("drop table Greetings");
        }
    }

    public static Connection getConnection() throws SQLException, IOException {

        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("D:\\005_some_code\\Java\\IdeaProjects\\JavaCorePart2\\src\\com\\company\\ch5\\test\\database.properties"))) {
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) {
            System.getProperty("jdbc.drivers", drivers);
        }
        try {
            Class.forName(drivers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        /*
        String drivers = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(drivers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/corejava";
        String username = "root";
        String password = "4546";
         */
        return DriverManager.getConnection(url, username, password);
    }
}
