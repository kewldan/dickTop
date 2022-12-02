package com.kewldan.dick.commands;

import com.kewldan.dick.DickTop;
import com.kewldan.dick.database.SQL;
import com.kewldan.dick.database.User;
import com.kewldan.dick.misc.CommandParent;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

@CommandParent(
        aliases = {"world", "мир"},
        description = "Длина писюнов игроков на сервере"
)
public class World {
    public static void run(Player sender, String[] args, User author) {
        try {
            ResultSet rs = SQL.executeQuery("SELECT SUM(dick) as dick FROM users;");
            if(rs.next()) {
                int total = rs.getInt("dick");
                sender.sendMessage(DickTop.getMessage("messages.world.result")
                        .replace("{LENGTH}", String.valueOf(total)));
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean visible(Player sender, User user) {
        return true;
    }
}
