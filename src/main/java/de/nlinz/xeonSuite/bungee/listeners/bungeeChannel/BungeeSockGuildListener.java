package de.nlinz.xeonSuite.bungee.listeners.bungeeChannel;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.keks.socket.bungee.BungeePlugin;
import de.keks.socket.bungee.events.plugin.BungeeSockGuildEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.keks.socket.core.Channel;
import de.nlinz.xeonSuite.bungee.dbase.PlayerHashDB;
import de.nlinz.xeonSuite.bungee.managers.PlayerManager;
import de.nlinz.xeonSuite.bungee.out.TeleportToGuild;
import de.nlinz.xeonSuite.bungee.utils.Location;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeSockGuildListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSocketMessage(BungeeSockGuildEvent e) {
		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equalsIgnoreCase("createGuild")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String guildName = in.readUTF();
				String guildMaster = in.readUTF();
				createGuild(guildUUID, guildName, guildMaster);
				return;
			}

			if (task.equalsIgnoreCase("deleteGuild")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				deleteGuild(guildUUID);
				return;
			}
			if (task.equalsIgnoreCase("UpdateGuildMaster")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String oldMaster = in.readUTF();
				String newMaster = in.readUTF();
				updateGuildMaster(guildUUID, oldMaster, newMaster);
				return;
			}
			if (task.equalsIgnoreCase("AddGuildToPlayer")) {
				String pname = in.readUTF();
				UUID guildUUID = UUID.fromString(in.readUTF());
				String gRang = in.readUTF();
				addPlayerToGuild(pname, guildUUID, gRang);
				return;
			}
			if (task.equalsIgnoreCase("DeleteGuildFromPlayer")) {
				String pname = in.readUTF();
				String uuid = in.readUTF();
				deletePlayerFromGuild(pname, uuid);
				return;
			}

			if (task.equals("SendGuildInvite")) {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(in.readUTF());
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
				String guildName = in.readUTF();
				UUID guildUUID = UUID.fromString(in.readUTF());

				if (invitedPlayer == null) {
					player.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht online!");
					return;
				}
				invitedPlayer.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.GREEN
						+ " hat dir eine Einladung in die Gilde " + ChatColor.YELLOW + guildName + ChatColor.GREEN
						+ " gesendet. " + ChatColor.YELLOW + "/guild accept " + ChatColor.GREEN + "um anzunehmen!");
				PlayerHashDB.guildInvites.put(invitedPlayer.getUniqueId(), guildUUID);
				return;
			}
			if (task.equals("AcceptGuildInvite")) {
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
				if (PlayerHashDB.guildInvites.containsKey(invitedPlayer.getUniqueId())) {
					UUID guildUUID = PlayerHashDB.guildInvites.get(invitedPlayer.getUniqueId());
					finishInvite(invitedPlayer, guildUUID);
				} else {
					invitedPlayer.sendMessage(ChatColor.RED + "Du hast keine offenen Einladungen!");
				}
				PlayerHashDB.guildInvites.remove(invitedPlayer.getUniqueId());

			}

			if (task.equals("UpdateGuildSpawn")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String server = in.readUTF();
				String world = in.readUTF();
				double x = in.readDouble();
				double y = in.readDouble();
				double z = in.readDouble();
				float yaw = in.readFloat();
				float pitch = in.readFloat();

				updateGuildSpawn(guildUUID, server, world, x, y, z, yaw, pitch);

			}

			if (task.equals("TeleportToGuildSpawn")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				TeleportToGuild.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(),
						in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to guild!");
				return;
			}

			if (task.equals("SendExpUpdate")) {
				String server = in.readUTF();
				UUID guildUUID = UUID.fromString(in.readUTF());
				long exp = in.readLong();
				long totalXp = in.readLong();
				sendExpUpdate(server, guildUUID, exp, totalXp);

			}

			if (task.equals("UpdateGuildName")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String guildName = in.readUTF();
				updateGuildName(guildUUID, guildName);

			}

			if (task.equals("UpdateGuildPlayer")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String player = in.readUTF();
				String rang = in.readUTF();
				updateGuildPlayer(guildUUID, player, rang);

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void deletePlayerFromGuild(String pname, String uuid)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("DeleteGuildFromPlayer");
			out.writeUTF(pname);
			out.writeUTF(uuid);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void addPlayerToGuild(String pname, UUID guildUUID, String gRang)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("AddGuildToPlayer");
			out.writeUTF(pname);
			out.writeUTF(guildUUID.toString());
			out.writeUTF(gRang);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void updateGuildMaster(UUID guildUUID, String oldMaster, String newMaster)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("UpdateGuildMaster");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(oldMaster);
			out.writeUTF(newMaster);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void updateGuildPlayer(UUID guildUUID, String player, String rang)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("UpdateGuildPlayer");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(player);
			out.writeUTF(rang);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void deleteGuild(UUID guildUUID)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("deleteGuild");
			out.writeUTF(guildUUID.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void createGuild(UUID guildUUID, String guildName, String guildMaster)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("createGuild");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(guildName);
			out.writeUTF(guildMaster);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void finishInvite(ProxiedPlayer invitedPlayer, UUID guildUUID)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("FinishGuildInvite");
			out.writeUTF(invitedPlayer.getName());
			out.writeUTF(guildUUID.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void updateGuildSpawn(UUID guildUUID, String server, String world, double x, double y, double z,
			float yaw, float pitch)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("UpdateGuildSpawn");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(server);
			out.writeUTF(world);
			out.writeDouble(x);
			out.writeDouble(y);
			out.writeDouble(z);
			out.writeFloat(yaw);
			out.writeFloat(pitch);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void sendExpUpdate(String server, UUID guildUUID, long exp, long totalXP)

	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("SendExpUpdate");
			out.writeUTF(server);
			out.writeUTF(guildUUID.toString());
			out.writeLong(exp);
			out.writeLong(totalXP);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

	public static void updateGuildName(UUID guildUUID, String guildName)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("UpdateGuildName");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(guildName);

		} catch (IOException e) {
			e.printStackTrace();
		}

		BungeePlugin.instance().sendBytesOut(bytes);
	}

}
