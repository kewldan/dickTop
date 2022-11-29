package com.kewldan.dick;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    int id;
    String nickname;
    public int dick;

    User() {
        id = -1;
        dick = -1;
        nickname = "__NULL__";
    }

    void load(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        nickname = rs.getString("nickname");
        dick = rs.getInt("dick");
    }

    public static User create(String nickname){
        User u = new User();
        u.nickname = nickname;
        return u;
    }

    public static User load(String nickname) throws SQLException {
        User u = new User();
        u.load(SQL.executeQuery(String.format("SELECT * FROM users WHERE `nickname`='%s'", nickname)));
        return u;
    }

    public static User load(int id) throws SQLException {
        User u = new User();
        u.load(SQL.executeQuery(String.format("SELECT * FROM users WHERE `id`='%d'", id)));
        return u;
    }

    public void save() {
        try {
            if (isLoaded()) {
                SQL.execute(String.format("UPDATE `users` SET `dick`='%d' WHERE id='%s'",
                        dick, id));
            } else {
                SQL.execute(String.format("INSERT INTO `users`(`nickname`, `dick`) VALUES ('%s', '%d')",
                        nickname, dick));
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
        }catch(SQLException e){
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