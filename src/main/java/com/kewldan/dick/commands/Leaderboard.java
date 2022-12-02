package com.kewldan.dick.commands;

import com.kewldan.dick.DickTop;
import com.kewldan.dick.database.SQL;
import com.kewldan.dick.database.User;
import com.kewldan.dick.misc.CommandParent;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@CommandParent(
        aliases = {"top", "lb", "leaderboard", "топ", "таблица"},
        description = "Топ игроков"
)
public class Leaderboard {
    public static void run(Player sender, String[] args, User author) {
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet rs = SQL.executeQuery("SELECT * FROM users ORDER BY dick");
            while (rs.next()) {
                users.add(User.load(rs));
            }
            rs.close();

            int pages = (int) Math.ceil(users.size() / 10.f);
            int page = args.length >= 2 ? Integer.parseInt(args[1]) : 0;
            page = Math.max(page, 0);
            page = Math.min(page, pages);
            StringBuilder b = new StringBuilder();
            b.append(DickTop.getMessage("messages.top.title")
                    .replace("{PAGE}", String.valueOf(page + 1))
                    .replace("{PAGES}", String.valueOf(pages))).append('\n');
            for (int i = page * 10; i < page * 10 + 10 && i < users.size(); i++) {
                User u = users.get(i);
                b.append(DickTop.getMessage("messages.top.row")
                        .replace("{NUMBER}", String.valueOf(i + 1))
                        .replace("{NICKNAME}", u.nickname)
                        .replace("{LENGTH}", String.valueOf(u.dick))).append('\n');
            }
            sender.sendMessage(b.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean visible(Player sender, User user) {
        return true;
    }
}
