package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;
import de.kekshaus.cookieApi.bungee.out.ChatActions;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamChatEvent;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Channel("BungeeStreamChat")
public class BungeeStreamChatListener extends AbstractPacketListener {
	@SuppressWarnings("deprecation")
	@PacketHandler
	public void onCookieApiMessageEvent(BungeeStreamChatEvent event) throws IOException, SQLException {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String task = in.readUTF();

		if (task.equals("ChannelChat")) {
			String sender = in.readUTF();
			String text = in.readUTF();
			String prefix = in.readUTF();
			String suffix = in.readUTF();
			String channel = in.readUTF();
			ChatActions.channelSend(sender, text, prefix, suffix, channel);
			return;
		}

		if (task.equals("ChannelSwitch")) {
			String sender = in.readUTF();
			String channel = in.readUTF();
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
			if (player == null) {
				return;
			}
			PlayerHashDB.channel.put(player.getUniqueId(), channel);
			return;
		}

		if (task.equals("SetAfk")) {
			String sender = in.readUTF();
			boolean value = in.readBoolean();
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
			if (player == null) {
				return;
			}
			if (value) {
				PlayerHashDB.isafk.put(player.getUniqueId(), value);
			} else {
				PlayerHashDB.isafk.remove(player.getUniqueId());
			}
			return;
		}

		if (task.equals("SocialSpy")) {
			String sender = in.readUTF();
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
			if (player == null) {
				return;
			}
			if (!PlayerHashDB.socialspy.containsKey(player.getUniqueId())) {
				PlayerHashDB.socialspy.put(player.getUniqueId(), true);
				player.sendMessage("§aChannel: ALLE (SocialSpy on)");

			} else {
				PlayerHashDB.socialspy.remove(player.getUniqueId());
				player.sendMessage("§aChannel: EIGENE (SocialSpy off)");
			}
			return;
		}

		if (task.equals("GuildChat")) {
			String guild = in.readUTF();
			String sender = in.readUTF();
			String text = in.readUTF();
			ChatActions.sendGuildChat(guild, sender, text);
			return;
		}

		if (task.equals("PrivateMsg")) {
			String sender = in.readUTF();
			String reciever = in.readUTF();
			String text = in.readUTF();
			String prefix = in.readUTF();
			ChatActions.privateMsgChat(sender, reciever, text, prefix);
			return;
		}

		if (task.equals("PrivateReply")) {
			String sender = in.readUTF();
			String text = in.readUTF();
			String prefix = in.readUTF();
			ChatActions.privateReplyChat(sender, text, prefix);
			return;
		}

	}

}