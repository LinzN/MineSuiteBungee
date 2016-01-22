
package de.kekshaus.cookieApi.bungee.managers;

import java.util.UUID;

import de.kekshaus.cookieApi.bungee.dbase.DataBaseActions;
import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;
import de.kekshaus.cookieApi.bungee.out.SendMsg;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MuteManager {

	public static boolean isMuted(final UUID uuid) {
		if (!PlayerHashDB.isMuted.containsKey(uuid)) {
			if (DataBaseActions.isMuted(uuid)) {
				PlayerHashDB.isMuted.put(uuid, true);
			} else {
				PlayerHashDB.isMuted.put(uuid, false);
			}
		}
		return PlayerHashDB.isMuted.get(uuid);

	}

	public static long getMuteTime(final UUID uuid) {
		if (PlayerHashDB.muteTime.containsKey(uuid)) {
			if (PlayerHashDB.muteTime.get(uuid) != -1L) {
				return (PlayerHashDB.muteTime.get(uuid) + 1000);
			} else {
				return PlayerHashDB.muteTime.get(uuid);
			}
		}
		return -1L;

	}

	public static String getMuteReason(final UUID uuid) {
		if (PlayerHashDB.muteReason.containsKey(uuid)) {
			return PlayerHashDB.muteReason.get(uuid);
		}
		return "";

	}

	public static String getMutedBy(final UUID uuid) {
		if (PlayerHashDB.mutedBy.containsKey(uuid)) {
			return PlayerHashDB.mutedBy.get(uuid);
		}
		return "";

	}

	@SuppressWarnings("deprecation")
	public static void mutePlayer(final UUID uuid, final String reason, final String mutedby, final long seconds,
			final String pname) {
		final long current = System.currentTimeMillis();
		long end = current + seconds * 1000L;
		if (seconds == -1L) {
			end = -1L;
		}
		DataBaseActions.mutePlayer(uuid, reason, mutedby, end);
		if (!isMuted(uuid)) {
			PlayerHashDB.isMuted.put(uuid, true);
		}

		PlayerHashDB.muteTime.put(uuid, end);
		PlayerHashDB.muteReason.put(uuid, reason);
		PlayerHashDB.mutedBy.put(uuid, mutedby);

		final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
		String mtime = MuteManager.getRemainingTime(uuid);
		if (target != null) {

			target.sendMessage(getMutedMessageNew(reason, mutedby, mtime));
		}
		if (seconds == -1L) {
			SendMsg.permMuteMSG(pname, reason, mutedby);
		} else {

			SendMsg.tempMuteMSG(pname, mtime, reason, mutedby);
		}

	}

	public static void unMute(final String player, final String reason, final String unmutedby) {
		UUID uuid = DataBaseActions.getUUID(player);
		if (isMuted(uuid)) {
			DataBaseActions.unmutePlayer(uuid, reason, unmutedby);
			PlayerHashDB.isMuted.remove(uuid);
			if (PlayerHashDB.muteTime.containsKey(uuid)) {
				PlayerHashDB.muteTime.remove(uuid);
			}
			if (PlayerHashDB.muteReason.containsKey(uuid)) {
				PlayerHashDB.muteReason.remove(uuid);
			}
			if (PlayerHashDB.mutedBy.containsKey(uuid)) {
				PlayerHashDB.mutedBy.remove(uuid);
			}
			SendMsg.unMute(player, reason, unmutedby);

		}
	}

	public static void unMute(final UUID uuid, final String reason, final String unmutedby, final String pname) {
		if (isMuted(uuid)) {
			DataBaseActions.unmutePlayer(uuid, reason, unmutedby);
			PlayerHashDB.isMuted.remove(uuid);
			if (PlayerHashDB.muteTime.containsKey(uuid)) {
				PlayerHashDB.muteTime.remove(uuid);
			}
			if (PlayerHashDB.muteReason.containsKey(uuid)) {
				PlayerHashDB.muteReason.remove(uuid);
			}
			if (PlayerHashDB.mutedBy.containsKey(uuid)) {
				PlayerHashDB.mutedBy.remove(uuid);
			}
			SendMsg.unMute(pname, reason, unmutedby);

		}
	}

	public static void unMuteSystem(final UUID uuid) {
		if (isMuted(uuid)) {
			DataBaseActions.unmutePlayer(uuid, "Abgelaufen", "SYSTEM");
			PlayerHashDB.isMuted.remove(uuid);
			if (PlayerHashDB.muteTime.containsKey(uuid)) {
				PlayerHashDB.muteTime.remove(uuid);
			}
			if (PlayerHashDB.muteReason.containsKey(uuid)) {
				PlayerHashDB.muteReason.remove(uuid);
			}
			if (PlayerHashDB.mutedBy.containsKey(uuid)) {
				PlayerHashDB.mutedBy.remove(uuid);
			}

		}
	}

	public static String getRemainingTime(final UUID uuid) {
		String remainingTime = "";
		final long current = System.currentTimeMillis();
		final long end = getMuteTime(uuid);
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

	public static String getMutedMessageNew(String reason, String mutedby, String time) {
		String MuteMsg;
		if (time.equalsIgnoreCase("Permanent")) {
			MuteMsg = "§6Du wurdest §aPermanent §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a"
					+ reason;
		} else {
			MuteMsg = "§6Du wurdest für §a" + time + " §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a"
					+ reason;
		}
		return MuteMsg;
	}

	public static String getMutedMessage(UUID uuid) {
		String MuteMsg;
		String time = getRemainingTime(uuid);
		String mutedby = getMutedBy(uuid);
		String reason = getMuteReason(uuid);
		if (time.equalsIgnoreCase("Permanent")) {
			MuteMsg = "§6Du bist §aPermanent §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a" + reason;
		} else {
			MuteMsg = "§6Du bist für §a" + time + " §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a"
					+ reason;
		}
		return MuteMsg;
	}
}
