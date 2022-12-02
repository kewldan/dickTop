package com.kewldan.dick;

import com.kewldan.dick.database.SQL;
import com.kewldan.dick.handlers.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class DickTop extends JavaPlugin {
    Connection connection;
    public final Logger logger = Bukkit.getLogger();
    public static FileConfiguration config;
    public static Server server;
    public static Commands commands;

    public static String getMessage(String code) {
        return config.contains(code) ? config.getString(code) : code;
    }

    @Override
    public void onEnable() {
        config = getConfig();
        server = getServer();

        File configFile = new File(getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
            logger.info("Created new config, use defaults!");
        }

        String url = "jdbc:mysql://" + getConfig().getString("mysql.host") + ":" + getConfig().getInt("mysql.port") + "/" + getConfig().getString("mysql.database") + "?autoReconnect=true&useSSL=false";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            logger.warning("MySQL didn't load");
        }
        try {
            connection = DriverManager.getConnection(url, getConfig().getString("mysql.user"), getConfig().getString("mysql.password"));
            logger.info("Database is OK");

            SQL.setConnection(connection, getClassLoader());
        } catch (SQLException e) {
            e.printStackTrace();

            getPluginLoader().disablePlugin(this);
        }

        commands = new Commands();
        commands.load();

        PluginCommand dick = getCommand("dick");

        if (dick != null) {
            dick.setTabCompleter(commands);
            dick.setExecutor(commands);
        }

        logger.info("Plugin [DickTop] successfully loaded");
    }

    @Override
    public void onDisable() {
        logger.info("Bye sweet <3");
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}