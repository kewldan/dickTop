package com.kewldan.dick.commands;

import com.kewldan.dick.DickTop;
import com.kewldan.dick.database.User;
import com.kewldan.dick.misc.CommandParent;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

@CommandParent(
        aliases = {"reward", "вырастить"},
        description = "Вырастить писюн"
)
public class Reward {
    private static final SimpleDateFormat sdf;

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    static {
        sdf = new SimpleDateFormat("HH:mm:ss");
    }

    public static void run(Player sender, String[] args, User author) {
        long passed = System.currentTimeMillis() - author.time;
        if (passed > 30000) {
            int n = getRandomNumber(DickTop.config.getInt("config.min"), DickTop.config.getInt("config.max"));
            author.dick += n;
            author.time = System.currentTimeMillis();
            author.save();
            if(n != 0) {
                sender.sendMessage(DickTop.getMessage("messages.reward.changed")
                        .replace("{VALUE}", String.valueOf(n))
                        .replace("{LENGTH}", String.valueOf(author.dick)));
            }else{
                sender.sendMessage(DickTop.getMessage("messages.reward.zero")
                        .replace("{LENGTH}", String.valueOf(author.dick)));
            }
        } else {
            sender.sendMessage(DickTop.getMessage("messages.reward.claimed")
                    .replace("{TIME}", sdf.format(30000L - 10800000L - passed))
                    .replace("{LENGTH}", String.valueOf(author.dick)));
        }
    }

    public static boolean visible(Player sender, User user) {
        return true;
    }
}
