package com.kewldan.dick.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    int id;
    public String nickname;
    public int dick;
    public long time;

    private User() {
        id = -1;
        dick = 0;
        nickname = "__NULL__";
        time = 0;
    }

    void ld(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        nickname = rs.getString("nickname");
        dick = rs.getInt("dick");
        time = rs.getLong("time");
    }

    public static User create(String nickname) {
        User u = new User();
        u.nickname = nickname;
        return u;
    }

    public static User load(String nickname) throws SQLException {
        User u = new User();
        ResultSet rs = SQL.executeQuery(String.format("SELECT * FROM users WHERE `nickname`='%s'", nickname));
        rs.next();
        u.ld(rs);
        rs.close();
        return u;
    }

    public static User load(int id) throws SQLException {
        User u = new User();
        ResultSet rs = SQL.executeQuery(String.format("SELECT * FROM users WHERE `id`='%d'", id));
        rs.next();
        u.ld(rs);
        rs.close();
        return u;
    }

    public static User load(ResultSet rs) throws SQLException {
        User u = new User();
        u.ld(rs);
        return u;
    }

    public void save() {
        try {
            if (isLoaded()) {
                SQL.execute(String.format("UPDATE `users` SET `dick`='%d', `time`='%d' WHERE id='%s'",
                        dick, time, id));
            } else {
                SQL.execute(String.format("INSERT INTO `users`(`nickname`, `dick`, `time`) VALUES ('%s', '%d', '%d')",
                        nickname, dick, time));
                ResultSet rs = SQL.executeQuery("SELECT Max(id) AS MaxID FROM users");
                try {
                    if (rs.next()) {
                        id = rs.getInt("MaxID");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            SQL.execute(String.format("DELETE FROM users WHERE `id`='%d'", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLoaded() {
        return id != -1;
    }
}