package com.kewldan.dick.commands;

import com.kewldan.dick.DickTop;
import com.kewldan.dick.database.User;
import com.kewldan.dick.handlers.Commands;
import com.kewldan.dick.misc.CommandBundle;
import com.kewldan.dick.misc.CommandParent;
import org.bukkit.entity.Player;

@CommandParent(
        aliases = {"help", "помощь"},
        description = "Список доступных команд"
)
public class Help {
    public static void run(Player sender, String[] args, User author) {
        StringBuilder b = new StringBuilder();
        b.append(DickTop.getMessage("messages.help.title")).append('\n');
        int j = 1;
        for (int i = 0; i < Commands.commands.size(); i++) {
            CommandBundle bundle = Commands.commands.get(i);
            if (bundle.canVisible(sender, author)) {
                b.append(DickTop.getMessage("messages.help.row")
                        .replace("{NUMBER}", String.valueOf(j))
                        .replace("{ALIAS}", bundle.command.aliases()[0])
                        .replace("{DESCRIPTION}", bundle.command.description())).append('\n');
                j++;
            }
        }
        sender.sendMessage(b.toString());
    }
}