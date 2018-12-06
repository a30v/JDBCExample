package com.example;

import java.sql.*;
import java.util.Properties;

public class Main {

    private static final String CREATE_QUERY =
            "create table if not exists example (greeting varchar(20), target varchar(20), date datetime not null)";

    private static final String DATA_QUERY =
            "insert into example values ('Hello','World', now())";

    public static void main(String[] args) {

        Properties prop = new Properties();
        prop.setProperty("user", "root");
        prop.setProperty("password", "root");
        prop.setProperty("serverTimezone", "UTC");
        prop.setProperty("useUnicode", "true");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/example", prop)) {
            try (Statement stat = conn.createStatement()) {
                stat.execute(CREATE_QUERY);
                stat.execute(DATA_QUERY);
            }

            try (PreparedStatement preStat =
                         conn.prepareStatement("select * FROM example")) {
                ResultSet rs = preStat.executeQuery();
                while (rs.next()) {
                    System.out.println(String.format("%s, %s!, %s",
                            rs.getString(1),
                            rs.getString("target"),
                            rs.getDate("date")));
                }
                rs.close();
            }
        } catch (SQLException ex) {
            System.out.println("Database connection failure: "
                    + ex.getMessage());
        }
    }
}
