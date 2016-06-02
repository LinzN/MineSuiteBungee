
package de.nlinz.xeonSuite.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.nlinz.xeonSuite.bungee.XeonSuiteBungee;
import net.md_5.bungee.config.Configuration;
import java.io.File;

public class Config {
	public static File ConfigFile;
	public static Configuration ConfigConfiguration;
	private XeonSuiteBungee instance;

	public Config(final XeonSuiteBungee instance) {
		this.instance = instance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
			Config.ConfigConfiguration = ConfigurationProvider.getProvider((Class) YamlConfiguration.class)
					.load(Config.ConfigFile);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		final Configuration config = Config.ConfigConfiguration;
		if (config.get("sql.host") == null) {
			config.set("sql.host", (Object) "localhost");
			config.set("sql.port", (Object) 3306);
			config.set("sql.username", (Object) "BungeeBan");
			config.set("sql.password", (Object) "SafePassword");
			config.set("sql.database", (Object) "BungeeBan");

			final List<String> ForbittenCMD = new ArrayList<String>();
			ForbittenCMD.add("g");
			ForbittenCMD.add("s");
			ForbittenCMD.add("global");
			ForbittenCMD.add("Global");
			config.set("cmd.forbidden", (Object) ForbittenCMD);

			config.set("p2p.ip", (Object) "127.0.0.1");
			config.set("p2p.port", (Object) 2222);
			config.set("p2p.castport", (Object) 6789);
			config.set("p2p.castip", (Object) "224.0.0.1");

			saveConfig(config, Config.ConfigFile);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void saveConfig(final Configuration config, final File file) {
		try {
			ConfigurationProvider.getProvider((Class) YamlConfiguration.class).save(config, file);
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
