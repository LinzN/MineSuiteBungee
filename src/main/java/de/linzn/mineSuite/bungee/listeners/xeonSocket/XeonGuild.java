package de.linzn.mineSuite.bungee.listeners.xeonSocket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.dbase.BungeeDataTable;
import de.linzn.mineSuite.bungee.managers.PlayerManager;
import de.linzn.mineSuite.bungee.out.TeleportToGuild;
import de.linzn.mineSuite.bungee.utils.Location;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.*;
import java.util.UUID;

public class XeonGuild implements IncomingDataListener {

	public static void deletePlayerFromGuild(String pname, String uuid)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("DeleteGuildFromPlayer");
			dataOutputStream.writeUTF(pname);
			dataOutputStream.writeUTF(uuid);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void addPlayerToGuild(String pname, UUID guildUUID, String gRang)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("AddGuildToPlayer");
			dataOutputStream.writeUTF(pname);
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeUTF(gRang);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void updateGuildMaster(UUID guildUUID, String oldMaster, String newMaster)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("UpdateGuildMaster");
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeUTF(oldMaster);
			dataOutputStream.writeUTF(newMaster);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void updateGuildPlayer(UUID guildUUID, String player, String rang)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("UpdateGuildPlayer");
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeUTF(player);
			dataOutputStream.writeUTF(rang);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void deleteGuild(UUID guildUUID)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("deleteGuild");
			dataOutputStream.writeUTF(guildUUID.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void createGuild(UUID guildUUID, String guildName, String guildMaster)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("createGuild");
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeUTF(guildName);
			dataOutputStream.writeUTF(guildMaster);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void finishInvite(ProxiedPlayer invitedPlayer, UUID guildUUID)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("FinishGuildInvite");
			dataOutputStream.writeUTF(invitedPlayer.getName());
			dataOutputStream.writeUTF(guildUUID.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void updateGuildSpawn(UUID guildUUID, String server, String world, double x, double y, double z,
										float yaw, float pitch)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("UpdateGuildSpawn");
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeUTF(server);
			dataOutputStream.writeUTF(world);
			dataOutputStream.writeDouble(x);
			dataOutputStream.writeDouble(y);
			dataOutputStream.writeDouble(z);
			dataOutputStream.writeFloat(yaw);
			dataOutputStream.writeFloat(pitch);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void sendExpUpdate(String server, UUID guildUUID, long exp, long totalXP)

	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("SendExpUpdate");
			dataOutputStream.writeUTF(server);
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeLong(exp);
			dataOutputStream.writeLong(totalXP);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	public static void updateGuildName(UUID guildUUID, String guildName)

	{

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

		try {
			dataOutputStream.writeUTF("UpdateGuildName");
			dataOutputStream.writeUTF(guildUUID.toString());
			dataOutputStream.writeUTF(guildName);

		} catch (IOException e) {
			e.printStackTrace();
		}

		MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteGuild", byteArrayOutputStream.toByteArray());
	}

	@Override
	public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
		String subChannel = null;
		try {
			subChannel = in.readUTF();

			if (subChannel.equalsIgnoreCase("createGuild")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String guildName = in.readUTF();
				String guildMaster = in.readUTF();
				createGuild(guildUUID, guildName, guildMaster);
				return;
			}

			if (subChannel.equalsIgnoreCase("deleteGuild")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				deleteGuild(guildUUID);
				return;
			}
			if (subChannel.equalsIgnoreCase("UpdateGuildMaster")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String oldMaster = in.readUTF();
				String newMaster = in.readUTF();
				updateGuildMaster(guildUUID, oldMaster, newMaster);
				return;
			}
			if (subChannel.equalsIgnoreCase("AddGuildToPlayer")) {
				String pname = in.readUTF();
				UUID guildUUID = UUID.fromString(in.readUTF());
				String gRang = in.readUTF();
				addPlayerToGuild(pname, guildUUID, gRang);
				return;
			}
			if (subChannel.equalsIgnoreCase("DeleteGuildFromPlayer")) {
				String pname = in.readUTF();
				String uuid = in.readUTF();
				deletePlayerFromGuild(pname, uuid);
				return;
			}

			if (subChannel.equals("SendGuildInvite")) {
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
				BungeeDataTable.guildInvites.put(invitedPlayer.getUniqueId(), guildUUID);
				return;
			}
			if (subChannel.equals("AcceptGuildInvite")) {
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
				if (BungeeDataTable.guildInvites.containsKey(invitedPlayer.getUniqueId())) {
					UUID guildUUID = BungeeDataTable.guildInvites.get(invitedPlayer.getUniqueId());
					finishInvite(invitedPlayer, guildUUID);
				} else {
					invitedPlayer.sendMessage(ChatColor.RED + "Du hast keine offenen Einladungen!");
				}
				BungeeDataTable.guildInvites.remove(invitedPlayer.getUniqueId());

			}

			if (subChannel.equals("UpdateGuildSpawn")) {
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

			if (subChannel.equals("TeleportToGuildSpawn")) {
				ProxiedPlayer player = PlayerManager.getPlayer(in.readUTF());
				TeleportToGuild.execute(player, new Location(in.readUTF(), in.readUTF(), in.readDouble(),
						in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat()));
				ProxyServer.getInstance().getLogger().info("[" + player + "] <-> teleportet to guild!");
				return;
			}

			if (subChannel.equals("SendExpUpdate")) {
				String server = in.readUTF();
				UUID guildUUID = UUID.fromString(in.readUTF());
				long exp = in.readLong();
				long totalXp = in.readLong();
				sendExpUpdate(server, guildUUID, exp, totalXp);

			}

			if (subChannel.equals("UpdateGuildName")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String guildName = in.readUTF();
				updateGuildName(guildUUID, guildName);

			}

			if (subChannel.equals("UpdateGuildPlayer")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String player = in.readUTF();
				String rang = in.readUTF();
				updateGuildPlayer(guildUUID, player, rang);

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
