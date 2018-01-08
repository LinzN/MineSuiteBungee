
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
		if (config.get("sql.host") == null) {
			config.set("sql.host", "localhost");
			config.set("sql.port", 3306);
			config.set("sql.username", "BungeeBan");
			config.set("sql.password", "SafePassword");
			config.set("sql.database", "BungeeBan");

			final List<String> ForbittenCMD = new ArrayList<String>();
			ForbittenCMD.add("g");
			ForbittenCMD.add("s");
			ForbittenCMD.add("global");
			ForbittenCMD.add("Global");
			config.set("cmd.forbidden", ForbittenCMD);

			saveConfig(config, Config.ConfigFile);
		}
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
}
