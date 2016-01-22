package de.kekshaus.cookieApi.bungee.managers;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.out.TPAFinalise;
import de.kekshaus.cookieApi.bungee.out.TeleportToLocation;
import de.kekshaus.cookieApi.bungee.out.TeleportToPlayer;
import de.kekshaus.cookieApi.bungee.utils.Location;
import de.kekshaus.cookieApi.bungee.utils.MessageDB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@SuppressWarnings("deprecation")
public class TeleportManager {
	public static HashMap<ProxiedPlayer, ProxiedPlayer> pendingTeleportsTPA = new HashMap<>(); // Player
																								// ----teleported--->
																								// player
	public static HashMap<ProxiedPlayer, ProxiedPlayer> pendingTeleportsTPAHere = new HashMap<>(); // Player
																									// ----teleported--->
																									// player
	private static int expireTime = 10;

	public static void requestToTeleportToPlayer(String player, String target) {
		final ProxiedPlayer bp = PlayerManager.getPlayer(player);
		final ProxiedPlayer bt = PlayerManager.getPlayer(target);
		if (playerHasPendingTeleport(bp)) {
			bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING);
			return;
		}
		if (bt == null) {
			bp.sendMessage(MessageDB.PLAYER_NOT_ONLINE.replace("{player}", bp.getName()));
			return;
		}
		if (bp.getName().equals(bt.getName())) {
			bp.sendMessage(MessageDB.TELEPORT_UNABLE.replace("{player}", bp.getName()));
			return;
		}
		if (playerHasPendingTeleport(bt)) {
			bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING_OTHER);
			return;
		}

		pendingTeleportsTPA.put(bt, bp);
		bp.sendMessage(MessageDB.TELEPORT_REQUEST_SENT.replace("{player}", bt.getName()));
		bt.sendMessage(MessageDB.PLAYER_REQUESTS_TO_TELEPORT_TO_YOU.replace("{player}", bp.getName()));
		ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, new Runnable() {
			@Override
			public void run() {
				if (pendingTeleportsTPA.containsKey(bt)) {
					if (!pendingTeleportsTPA.get(bt).equals(bp)) {
						return;
					}
					if (bp != null) {
						bp.sendMessage(MessageDB.TPA_REQUEST_TIMED_OUT.replace("{player}", bt.getName()));
					}
					pendingTeleportsTPA.remove(bt);
					if (bt != null) {
						bt.sendMessage(MessageDB.TP_REQUEST_OTHER_TIMED_OUT.replace("{player}", bp.getName()));
					}
				}
			}
		}, expireTime, TimeUnit.SECONDS);
	}

	public static void requestPlayerTeleportToYou(String player, String target) {
		final ProxiedPlayer bp = PlayerManager.getPlayer(player);
		final ProxiedPlayer bt = PlayerManager.getPlayer(target);
		if (playerHasPendingTeleport(bp)) {
			bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING);
			return;
		}
		if (bt == null) {
			bp.sendMessage(MessageDB.PLAYER_NOT_ONLINE.replace("{player}", bp.getName()));
			return;
		}
		if (playerHasPendingTeleport(bt)) {
			bp.sendMessage(MessageDB.PLAYER_TELEPORT_PENDING_OTHER.replace("{player}", bt.getName()));
			return;
		}
		pendingTeleportsTPAHere.put(bt, bp);
		bp.sendMessage(MessageDB.TELEPORT_REQUEST_SENT.replace("{player}", bp.getName()));
		bt.sendMessage(MessageDB.PLAYER_REQUESTS_YOU_TELEPORT_TO_THEM.replace("{player}", bp.getName()));

		ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, new Runnable() {
			@Override
			public void run() {
				if (pendingTeleportsTPAHere.containsKey(bt)) {
					if (!pendingTeleportsTPAHere.get(bt).equals(bp)) {
						return;
					}
					if (bp != null) {
						bp.sendMessage(MessageDB.TPAHERE_REQUEST_TIMED_OUT.replace("{player}", bt.getName()));
					}
					pendingTeleportsTPAHere.remove(bt);
					if (bt != null) {
						bt.sendMessage(MessageDB.TP_REQUEST_OTHER_TIMED_OUT.replace("{player}", bp.getName()));
					}
				}
			}
		}, expireTime, TimeUnit.SECONDS);
	}

	public static void acceptTeleportRequest(ProxiedPlayer player) {
		if (pendingTeleportsTPA.containsKey(player)) {
			ProxiedPlayer target = pendingTeleportsTPA.get(player);
			player.sendMessage(MessageDB.TELEPORT_ACCEPTED.replace("{player}", target.getName()));
			target.sendMessage(MessageDB.TELEPORT_REQUEST_ACCEPTED.replace("{player}", player.getName()));
			TPAFinalise.execute(target, player);
			pendingTeleportsTPA.remove(player);
		} else if (pendingTeleportsTPAHere.containsKey(player)) {
			ProxiedPlayer target = pendingTeleportsTPAHere.get(player);
			player.sendMessage(MessageDB.TELEPORT_ACCEPTED.replace("{player}", target.getName()));
			target.sendMessage(MessageDB.TELEPORT_REQUEST_ACCEPTED.replace("{player}", player.getName()));
			TPAFinalise.execute(player, target);
			pendingTeleportsTPAHere.remove(player);
		} else {
			PlayerManager.sendMessageToTarget(player, MessageDB.NO_TELEPORTS);
		}
	}

	public static void denyTeleportRequest(ProxiedPlayer player) {
		if (pendingTeleportsTPA.containsKey(player)) {
			ProxiedPlayer target = pendingTeleportsTPA.get(player);
			PlayerManager.sendMessageToTarget(player, MessageDB.TELEPORT_DENIED.replace("{player}", target.getName()));
			target.sendMessage(MessageDB.TELEPORT_REQUEST_DENIED.replace("{player}", player.getName()));
			pendingTeleportsTPA.remove(player);
		} else if (pendingTeleportsTPAHere.containsKey(player)) {
			ProxiedPlayer target = pendingTeleportsTPAHere.get(player);
			PlayerManager.sendMessageToTarget(player, MessageDB.TELEPORT_DENIED.replace("{player}", target.getName()));
			target.sendMessage(MessageDB.TELEPORT_REQUEST_DENIED.replace("{player}", player.getName()));
			pendingTeleportsTPAHere.remove(player);
		} else {
			PlayerManager.sendMessageToTarget(player, MessageDB.NO_TELEPORTS);
		}
	}

	public static boolean playerHasPendingTeleport(ProxiedPlayer player) {
		return pendingTeleportsTPA.containsKey(player) || pendingTeleportsTPAHere.containsKey(player);
	}

	public static void setPlayersDeathBackLocation(ProxiedPlayer player, Location loc) {
		PlayerManager.setDeathBackLocation(loc, player);
	}

	public static void sendPlayerToLastBack(ProxiedPlayer player) {
		if (PlayerManager.hasDeathBackLocation(player)) {
			TeleportToLocation.execute(player, PlayerManager.getLastBackLocation(player));
			ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to deathpoint!");
			PlayerManager.removeDeathBackLocation(player);
		} else {
			PlayerManager.sendMessageToTarget(player, MessageDB.NO_BACK_TP);
		}

	}

	public static void tpAll(String sender, String target) {
		ProxiedPlayer p = PlayerManager.getPlayer(sender);
		ProxiedPlayer t = PlayerManager.getPlayer(target);

		if (t == null) {
			p.sendMessage(MessageDB.PLAYER_NOT_ONLINE);
			return;
		}

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			if (!player.equals(p)) {
				TeleportToPlayer.execute(p, t);
			}

			PlayerManager.sendMessageToTarget(player,
					MessageDB.ALL_PLAYERS_TELEPORTED.replace("{player}", t.getName()));
		}
	}

	public static void teleportPlayerToPlayer(String player, String target, boolean silent, boolean bypass) {
		ProxiedPlayer p = PlayerManager.getPlayer(player);
		ProxiedPlayer t = PlayerManager.getPlayer(target);
		if (p == null || t == null) {
			p.sendMessage(MessageDB.PLAYER_NOT_ONLINE);
			return;
		}

		TeleportToPlayer.execute(p, t);

		if (!silent) {
			t.sendMessage(MessageDB.PLAYER_TELEPORTED_TO_YOU.replace("{player}", p.getName()));
		}

		p.sendMessage(MessageDB.TELEPORTED_TO_PLAYER.replace("{player}", t.getName()));
	}
}
