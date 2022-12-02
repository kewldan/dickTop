package com.kewldan.dick.handlers;

import com.kewldan.dick.DickTop;
import com.kewldan.dick.database.User;
import com.kewldan.dick.misc.CommandBundle;
import com.kewldan.dick.misc.CommandParent;
import com.sun.istack.internal.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class Commands implements CommandExecutor, TabCompleter {
    public static HashMap<String, Method> aliases;
    public static List<CommandBundle> commands;

    public void load() {
        Reflections reflections = new Reflections("com.kewldan.dick.commands");
        Set<Class<?>> annotated =
                reflections.get(SubTypes.of(TypesAnnotated.with(CommandParent.class)).asClass());
        aliases = new HashMap<>();
        commands = new ArrayList<>();
        for (Class<?> c : annotated) {
            try {
                CommandParent annotation = c.getAnnotation(CommandParent.class);
                Method method = c.getMethod("run", Player.class, String[].class, User.class);
                for (String a : annotation.aliases()) {
                    aliases.put(a, method);
                }
                Method visible = null;
                try {
                    visible = c.getMethod("visible", Player.class, User.class);
                } catch (NoSuchMethodException ignored) {

                }
                commands.add(new CommandBundle(annotation, visible));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String cmd = args.length >= 1 ? args[0].toLowerCase() : "reward";

            if (aliases.containsKey(cmd)) {
                User u;
                try {
                    u = User.load(sender.getName());

                    if (!u.isLoaded()) {
                        u = User.create(sender.getName());
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    return false;
                }

                try {
                    aliases.get(cmd).invoke(null, player, args, u);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(DickTop.getMessage("messages.commands.unknown").replace("{CMD}", cmd));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> out = new ArrayList<>();
        if (args.length <= 1) {
            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }

            User u;
            try {
                u = User.load(sender.getName());

                if (!u.isLoaded()) {
                    u = User.create(sender.getName());
                }
            } catch (SQLException ignored) {
                return null;
            }

            for (CommandBundle cmd : commands) {
                if (cmd.canVisible(p, u)) {
                    out.add(cmd.command.aliases()[0]);
                }
            }
        }
        return out;
    }
}
