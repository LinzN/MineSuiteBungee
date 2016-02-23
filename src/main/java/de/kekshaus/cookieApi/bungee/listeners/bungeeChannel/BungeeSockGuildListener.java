package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.keks.socket.bungee.BungeePlugin;
import de.keks.socket.bungee.events.plugin.BungeeSockGuildEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.keks.socket.core.Channel;
import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;
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
				String guildOwner = in.readUTF();
				createGuild(guildUUID, guildName, guildOwner);
				return;
			}

			if (task.equalsIgnoreCase("deleteGuild")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				deleteGuild(guildUUID);
				return;
			}
			if (task.equalsIgnoreCase("UpdateGuildOwner")) {
				UUID guildUUID = UUID.fromString(in.readUTF());
				String owner = in.readUTF();
				updateGuildOwner(guildUUID, owner);
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
				deletePlayerFromGuild(pname);
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
			if (task.equals("GuildInfo")) {
				String guild = in.readUTF();
				String text = in.readUTF();
				sendGuildInfo(guild, text);
				return;
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void deletePlayerFromGuild(String pname)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("DeleteGuildFromPlayer");
			out.writeUTF(pname);

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

	public static void updateGuildOwner(UUID guildUUID, String owner)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("UpdateGuildOwner");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(owner);

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

	public static void createGuild(UUID guildUUID, String guildName, String guildOwner)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);

		try {
			out.writeUTF("createGuild");
			out.writeUTF(guildUUID.toString());
			out.writeUTF(guildName);
			out.writeUTF(guildOwner);

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

	public static void sendGuildInfo(String guild, String text) {
		ProxyServer.getInstance().getServers();
		String formatedText = "§6[§aGilde§6] INFO: §a" + text;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = Channel.guildChannel(bytes);
		try {
			out.writeUTF("GuildInfo");
			out.writeUTF(guild);
			out.writeUTF(formatedText);

		} catch (IOException e) {
			e.printStackTrace();
		}
		BungeePlugin.instance().sendBytesOut(bytes);
		ProxyServer.getInstance().getLogger().info(guild + "-> " + formatedText);

	}

}
