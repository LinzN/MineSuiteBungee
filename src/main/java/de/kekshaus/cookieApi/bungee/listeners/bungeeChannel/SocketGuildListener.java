package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;
import de.kekshaus.cookieApi.bungee.out.tasks.SendSocketGuildMessage;
import de.kekshaus.cookieApi.bungee.socketEvents.SocketGuildEvent;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Channel("SocketGuild")
public class SocketGuildListener extends AbstractPacketListener {
	@SuppressWarnings("deprecation")
	@PacketHandler
	public void onCookieApiMessageEvent(final SocketGuildEvent event) {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
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

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	public static void addToGuild(ProxiedPlayer invitedPlayer, UUID guildUUID)

	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bytes);

		try {
			out.writeUTF("FinishGuildInvite");
			out.writeUTF(invitedPlayer.getName());
			out.writeUTF(guildUUID.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

		CookieApiBungee.proxy.getScheduler().runAsync(CookieApiBungee.instance, new SendSocketGuildMessage(bytes));
	}

}