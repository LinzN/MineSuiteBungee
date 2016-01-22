
package de.kekshaus.cookieApi.bungee.managers;

import java.util.List;
import java.util.UUID;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseActions;
import de.kekshaus.cookieApi.bungee.out.SendMsg;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {
	@SuppressWarnings("deprecation")
	public static void banPlayer(final UUID uuid, final String reason, final String bannedby, final long seconds,
			final String pname) {
		final long current = System.currentTimeMillis();
		long end = current + seconds * 1000L;
		if (seconds == -1L) {
			end = -1L;
		}
		DataBaseActions.banPlayer(uuid, reason, bannedby, end);

		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
		String mtime = BanManager.getRemainingTime(end);
		if (player != null) {
			player.disconnect(getBannedMessageNew(reason, bannedby, mtime));
		}
		if (seconds == -1L) {
			SendMsg.permBanMSG(pname, reason, bannedby);
		} else {
			SendMsg.tempBanMSG(pname, mtime, reason, bannedby);
		}

	}

	public static boolean isBanned(ProxiedPlayer player) {
		return DataBaseActions.isBanned(player.getUniqueId());

	}

	public static boolean isBanned(UUID uuid) {
		return DataBaseActions.isBanned(uuid);

	}

	public static void unBan(final String player, final String reason, final String unbannedby) {
		UUID uuid = DataBaseActions.getUUID(player);
		if (isBanned(uuid)) {
			DataBaseActions.unbanPlayer(uuid, reason, unbannedby);
			SendMsg.unBan(player, reason, unbannedby);
		}
	}

	public static void unBan(final UUID uuid, final String reason, final String unbannedby, final String pname) {
		if (isBanned(uuid)) {
			DataBaseActions.unbanPlayer(uuid, reason, unbannedby);
			SendMsg.unBan(pname, reason, unbannedby);

		}
	}

	public static void unBanSystem(final UUID uuid) {
		if (isBanned(uuid)) {
			DataBaseActions.unbanPlayer(uuid, "Abgelaufen", "SYSTEM");

		}
	}

	public static long getEnd(final UUID uuid) {
		long end = -1L;
		end = DataBaseActions.getBanExpired(uuid);
		return end;
	}

	public static String getRemainingTime(long time) {
		String remainingTime = "";
		final long current = System.currentTimeMillis();
		final long end;
		if (time != -1L) {
			end = (time + 1000);
		} else {
			end = (time);
		}
		long difference = end - current;
		if (end == -1L) {
			return "Permanent";
		}
		int seconds = 0;
		int minutes = 0;
		int hours = 0;
		int days = 0;
		int weeks = 0;
		while (difference >= 1000L) {
			difference -= 1000L;
			++seconds;
		}
		while (seconds >= 60) {
			seconds -= 60;
			++minutes;
		}
		while (minutes >= 60) {
			minutes -= 60;
			++hours;
		}
		while (hours >= 24) {
			hours -= 24;
			++days;
		}
		while (days >= 7) {
			days -= 7;
			++weeks;
		}

		String w = "";
		String d = "";
		String h = "";
		String m = "";
		String s = "";
		if (weeks != 0) {
			w = "§e" + weeks + " Woche(n) ";
		}
		if (days != 0) {
			d = days + " Tag(e) ";
		}
		if (hours != 0) {
			h = hours + " Stunde(n) ";
		}
		if (minutes != 0) {
			m = minutes + " Minute(n) ";
		}
		if (seconds != 0) {
			s = seconds + " Sekunde(n)";
		}

		remainingTime = "§e" + w + d + h + m + s;

		return remainingTime;
	}

	public static String getBannedMessageNew(String reason, String bannedby, String time) {
		String BanMsg;
		if (time.equalsIgnoreCase("Permanent")) {
			BanMsg = "§6Du wurdest §aPermanent §6von §a" + bannedby + " §6vom Server gesperrt. \n§6Grund: §a" + reason;
		} else {
			BanMsg = "§6Du wurdest für §a" + time + " §6von §a" + bannedby + " §6vom Server gesperrt. \n§6Grund: §a"
					+ reason;
		}

		return BanMsg;
	}

	public static String getBannedMessage(UUID uuid) {
		List<String> list = DataBaseActions.getBanInfos(uuid);
		String reason = list.get(0);
		String bannedby = list.get(1);
		long milisec = Long.parseLong(list.get(2));
		String time = getRemainingTime(milisec);
		String BanMsg;
		if (time.equalsIgnoreCase("Permanent")) {
			BanMsg = "§6Du bist §aPermanent §6von §a" + bannedby + " §6vom Server gesperrt. \n§6Grund: §a" + reason;
		} else {
			BanMsg = "§6Du bist für §a" + time + " §6von §a" + bannedby + " §6vom Server gesperrt. \n§6Grund: §a"
					+ reason;
		}

		return BanMsg;
	}
}
