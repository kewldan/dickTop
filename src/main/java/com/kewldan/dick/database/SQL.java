package com.kewldan.dick.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
    static Connection connection;

    public static void setConnection(Connection connection, ClassLoader loader) throws SQLException {
        SQL.connection = connection;

        execute("CREATE TABLE IF NOT EXISTS `users` (`id` INT unsigned NOT NULL AUTO_INCREMENT,`nickname` TEXT, `time` BIGINT, `dick` INT DEFAULT '0',UNIQUE KEY `id` (`id`) USING BTREE,PRIMARY KEY (`id`));");
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public static void execute(String sql) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        statement.execute(sql);
    }
}
