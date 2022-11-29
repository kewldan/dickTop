package com.kewldan.dick;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandBundle {
    public CommandParent command;
    public Method visible;

    public CommandBundle(CommandParent command, Method visible) {
        this.command = command;
        this.visible = visible;
    }

    public boolean canVisible(Player sender, User user) {
        if (visible != null) {
            try {
                return (boolean) visible.invoke(null, sender, user);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
