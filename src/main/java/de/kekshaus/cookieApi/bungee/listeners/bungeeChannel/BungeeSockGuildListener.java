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

			if (task.equals("SendGuildInvite")) {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(in.readUTF());
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
				String guildName = in.readUTF();
				UUID guildUUID = UUID.fromString(in.readUTF());

				if (invitedPlayer == null) {
					player.sendMessage("Dieser Spieler ist nicht online!");
					return;
				}
				invitedPlayer.sendMessage(player.getName() + " hat dir eine Einladung in die Gilde " + guildName
						+ " gesendet. /guild accept um anzunehmen!");
				PlayerHashDB.guildInvites.put(invitedPlayer.getUniqueId(), guildUUID);
				return;
			}
			if (task.equals("AcceptGuildInvite")) {
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
				if (PlayerHashDB.guildInvites.containsKey(invitedPlayer.getUniqueId())) {
					UUID guildUUID = PlayerHashDB.guildInvites.get(invitedPlayer.getUniqueId());
					addToGuild(invitedPlayer, guildUUID);
				} else {
					invitedPlayer.sendMessage("Du hast keine offenen Einladungen!");
				}
				PlayerHashDB.guildInvites.remove(invitedPlayer.getUniqueId());

			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void addToGuild(ProxiedPlayer invitedPlayer, UUID guildUUID)

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

}
