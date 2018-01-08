package de.linzn.mineSuite.bungee.out;

import de.linzn.mineSuite.bungee.dbase.BungeeDataTable;
import de.linzn.mineSuite.bungee.listeners.xeonSocket.XeonChat;
import de.linzn.mineSuite.bungee.utils.ChatFormate;
import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ChatActions {

	@SuppressWarnings("deprecation")
	public static void sendGuildChat(String guild, String sender, String text) {
		ProxyServer.getInstance().getServers();
		String formatedText = ChatFormate.toGuildFormate(sender, text);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonChat.channelName);

		try {
			out.writeUTF("GuildChat");
			out.writeUTF(guild);
			out.writeUTF(formatedText);

		} catch (IOException e) {
			e.printStackTrace();
		}
		XeonSocketServerManager.sendData(bytes);
		ProxyServer.getInstance().getLogger().info(guild + "-> " + formatedText);
		for (UUID uuid : BungeeDataTable.socialspy.keySet()) {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
			p.sendMessage("§4[SPY]§r" + guild + "-> " + formatedText);
		}
	}

	@SuppressWarnings("deprecation")
	public static void channelSend(String sender, String text, String prefix, String suffix, String channel,
			String guild) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
		if (player == null) {
			return;
		}
		if (channel.equalsIgnoreCase("GLOBAL")) {
			globalChat(sender, text, prefix, suffix);
		} else if (channel.equalsIgnoreCase("STAFF")) {
			staffChat(sender, text, prefix);

		} else if (channel.equalsIgnoreCase("GUILD")) {
			sendGuildChat(guild, sender, text);

		} else if (channel.equalsIgnoreCase("NONE")) {
			String ch = BungeeDataTable.channel.get(player.getUniqueId());
			if (ch == null) {
				ProxyServer.getInstance().getLogger().info("Channel for player " + sender + " == null ????");
				return;
			}
			if (ch.equalsIgnoreCase("GLOBAL")) {
				globalChat(sender, text, prefix, suffix);
			} else if (ch.equalsIgnoreCase("STAFF")) {
				staffChat(sender, text, prefix);

			} else if (ch.equalsIgnoreCase("GUILD")) {
				if (guild.equalsIgnoreCase("NONE")) {
					player.sendMessage("Du bist in keiner Gilde!");
					BungeeDataTable.channel.put(player.getUniqueId(), "GLOBAL");
				} else {
					sendGuildChat(guild, sender, text);
				}
			}
		} else {
			globalChat(player.getDisplayName(), text, prefix, suffix);
		}

	}

	@SuppressWarnings("deprecation")
	public static void globalChat(String sender, String text, String prefix, String suffix) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);

		if (player == null) {
			return;
		}
		String formatedText = ChatFormate.toChannelGlobalFormate(sender, text, prefix, suffix);
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			p.sendMessage(formatedText);
		}
		ProxyServer.getInstance().getLogger().info(formatedText);

	}

	public static void staffChat(String sender, String text, String prefix) {
		ProxyServer.getInstance().getServers();
		String formatedText = ChatFormate.toChannelStaffFormate(sender, text, prefix);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonChat.channelName);
		try {
			out.writeUTF("StaffChat");
			out.writeUTF(formatedText);

		} catch (IOException e) {
			e.printStackTrace();
		}
		XeonSocketServerManager.sendData(bytes);
		ProxyServer.getInstance().getLogger().info("STAFF" + "-> " + formatedText);
	}

	@SuppressWarnings("deprecation")
	public static void privateMsgChat(String sender, String reciever, String text, String prefix) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
		ProxiedPlayer recievedPlayer = ProxyServer.getInstance().getPlayer(reciever);

		if (player == null) {
			return;
		}

		if (recievedPlayer == null) {
			player.sendMessage("Dieser Spieler ist nicht online!");
			return;
		}
		if (BungeeDataTable.isafk.containsKey(recievedPlayer.getUniqueId())) {
			player.sendMessage("§eDer Spieler ist als abwesend makiert!");
		}
		BungeeDataTable.msgreply.put(recievedPlayer.getUniqueId(), player.getUniqueId());

		String formatedTextSender = ChatFormate.toPrivateMsgSenderFormate(sender, reciever, text, prefix);
		String formatedTextReciever = ChatFormate.toPrivateMsgRecieverFormate(sender, reciever, text, prefix);
		String formatedText = ChatFormate.toPrivateMsgFormate(sender, reciever, text);

		player.sendMessage(formatedTextSender);
		recievedPlayer.sendMessage(formatedTextReciever);

		ProxyServer.getInstance().getLogger().info("[PM]" + formatedText);

		for (UUID uuid : BungeeDataTable.socialspy.keySet()) {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
			p.sendMessage("§4[SPY]§r" + formatedText);
		}

	}

	@SuppressWarnings("deprecation")
	public static void privateReplyChat(String sender, String text, String prefix) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
		UUID uuidreciever = BungeeDataTable.msgreply.get(player.getUniqueId());
		if (uuidreciever == null) {
			player.sendMessage("Du hast niemand zum Antworten!");
			return;
		}
		ProxiedPlayer recievedPlayer = ProxyServer.getInstance().getPlayer(uuidreciever);
		if (recievedPlayer == null) {
			player.sendMessage("Dieser Spieler ist offline!");
			return;
		}
		if (BungeeDataTable.isafk.containsKey(recievedPlayer.getUniqueId())) {
			player.sendMessage("§eDer Spieler ist als abwesend makiert!");
		}
		BungeeDataTable.msgreply.put(recievedPlayer.getUniqueId(), player.getUniqueId());

		String formatedTextSender = ChatFormate.toPrivateMsgSenderFormate(sender, recievedPlayer.getName(), text,
				prefix);
		String formatedTextReciever = ChatFormate.toPrivateMsgRecieverFormate(sender, recievedPlayer.getName(), text,
				prefix);
		String formatedText = ChatFormate.toPrivateMsgFormate(sender, recievedPlayer.getName(), text);

		player.sendMessage(formatedTextSender);
		recievedPlayer.sendMessage(formatedTextReciever);

		ProxyServer.getInstance().getLogger().info("[Reply]" + formatedText);
		for (UUID uuid : BungeeDataTable.socialspy.keySet()) {
			ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
			p.sendMessage("§4[SPY]§r" + formatedText);
		}
	}

}