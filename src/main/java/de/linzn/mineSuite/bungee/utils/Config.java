/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.bungee.utils;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static File ConfigFile;
    public static Configuration ConfigConfiguration;
    private MineSuiteBungeePlugin instance;

    public Config(final MineSuiteBungeePlugin instance) {
        this.instance = instance;
    }

    public static void saveConfig(final Configuration config, final File file) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer getInt(final String path) {
        final Configuration config = Config.ConfigConfiguration;
        if (config.getString(path) != null) {
            return config.getInt(path);
        }
        return 0;
    }

    public static String getString(final String path) {
        final Configuration config = Config.ConfigConfiguration;
        if (config.getString(path) != null) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(path));
        }
        return "§cString not found";
    }

    public static List<String> getStringList(final String path) {
        final Configuration config = Config.ConfigConfiguration;
        if (config.getStringList(path) != null) {
            return config.getStringList(path);
        }
        return null;
    }

    public void setDefaultConfig() {
        if (!this.instance.getDataFolder().exists()) {
            this.instance.getDataFolder().mkdir();
        }
        Config.ConfigFile = new File(this.instance.getDataFolder().getPath(), "config.yml");
        if (!Config.ConfigFile.exists()) {
            try {
                Config.ConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Config.ConfigConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(Config.ConfigFile);
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        final Configuration config = Config.ConfigConfiguration;
        if (config.get("mysql.host") == null) {
            config.set("mysql.host", "localhost");
            config.set("mysql.port", 3306);
            config.set("mysql.username", "BungeeBan");
            config.set("mysql.password", "SafePassword");
            config.set("mysql.database", "BungeeBan");


            config.set("jSocket.hostname", "localhost");
            config.set("jSocket.port", 9090);

            final List<String> ForbittenCMD = new ArrayList<String>();
            ForbittenCMD.add("g");
            ForbittenCMD.add("s");
            ForbittenCMD.add("l");
            ForbittenCMD.add("gc");
            ForbittenCMD.add("global");
            ForbittenCMD.add("Global");
            config.set("cmd.forbidden", ForbittenCMD);

            saveConfig(config, Config.ConfigFile);
        }
    }
}
