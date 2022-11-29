package com.kewldan.dick.handlers;

import com.kewldan.dick.DickTop;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class Events {
    public void load(DickTop dick) {
        Reflections reflections = new Reflections("com.kewldan.dick.events");
        Set<Class<?>> annotated = reflections.get(SubTypes.of(Object.class).asClass());
        for (Class<?> c : annotated) {
            try {
                DickTop.server.getPluginManager().registerEvents((Listener) c.getConstructors()[0].newInstance(), dick);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
