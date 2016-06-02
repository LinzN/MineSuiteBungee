package de.nlinz.xeonSuite.bungee.listeners.xeonSocket;

import java.io.DataInputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;
import de.nlinz.xeonSuite.bungee.dbase.PlayerHashDB;
import de.nlinz.xeonSuite.bungee.out.ChatActions;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class XeonChat implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonChat";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = XeonSocketServerManager.readDataInput(event.getStreamBytes());
		String task = null;
		try {
			task = in.readUTF();
			if (task.equals("ChannelChat")) {
				String sender = in.readUTF();
				String text = in.readUTF();
				String prefix = in.readUTF();
				String suffix = in.readUTF();
				String channel = in.readUTF();
				String guild = in.readUTF();
				ChatActions.channelSend(sender, text, prefix, suffix, channel, guild);
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
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
