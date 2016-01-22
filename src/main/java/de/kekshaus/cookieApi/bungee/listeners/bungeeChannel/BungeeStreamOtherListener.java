package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

import de.kekshaus.cookieApi.bungee.dbase.PlayerHashDB;
import de.kekshaus.cookieApi.bungee.out.GuildActions;
import de.kekshaus.cookieApi.bungee.socketEvents.BungeeStreamOtherEvent;
import de.xHyveSoftware.socket.bungee.api.annotation.Channel;
import de.xHyveSoftware.socket.bungee.api.annotation.PacketHandler;
import de.xHyveSoftware.socket.bungee.api.listener.AbstractPacketListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Channel("BungeeStreamOther")
public class BungeeStreamOtherListener extends AbstractPacketListener {
	@SuppressWarnings("deprecation")
	@PacketHandler
	public void onCookieApiMessageEvent(BungeeStreamOtherEvent event) throws IOException, SQLException {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String task = in.readUTF();

		if (task.equals("SendGuildInvite")) {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(in.readUTF());
			ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
			String guild = in.readUTF();
			if (invitedPlayer == null) {
				player.sendMessage("Dieser Spieler ist nicht online!");
				return;
			}
			invitedPlayer.sendMessage(player.getName() + " hat dir eine Einladung in die Gilde " + guild
					+ " gesendet. /guild accept um anzunehmen!");
			PlayerHashDB.guildInvites.put(invitedPlayer.getUniqueId(), guild);
			return;
		}
		if (task.equals("AcceptGuildInvite")) {
			ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(in.readUTF());
			if (PlayerHashDB.guildInvites.containsKey(invitedPlayer.getUniqueId())) {
				String guild = PlayerHashDB.guildInvites.get(invitedPlayer.getUniqueId());
				GuildActions.addToGuild(invitedPlayer, guild);
				invitedPlayer.sendMessage("Willkommen in der Gilde " + guild + "!");
			} else {
				invitedPlayer.sendMessage("Du hast keine offenen Einladungen!");
			}
			PlayerHashDB.guildInvites.remove(invitedPlayer.getUniqueId());

		}

	}

}