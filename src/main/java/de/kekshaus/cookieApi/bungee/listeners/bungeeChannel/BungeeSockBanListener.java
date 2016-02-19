package de.kekshaus.cookieApi.bungee.listeners.bungeeChannel;

import java.io.DataInputStream;
import java.io.IOException;

import de.keks.socket.bungee.events.plugin.BungeeSockBanEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.kekshaus.cookieApi.bungee.BanApi;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeSockBanListener implements Listener {
	@EventHandler
	public void onSocketMessage(BungeeSockBanEvent e) {
		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		try {
			task = in.readUTF();

			if (task.equals("PermaBan")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String bannedby = in.readUTF();
				BanApi.permBan(player, reason, bannedby);

				return;
			}
			if (task.equals("TempBan")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String bannedby = in.readUTF();
				Long seconds = in.readLong();
				BanApi.tempBan(player, reason, bannedby, seconds);
				return;
			}
			if (task.equals("PermaMute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String mutedby = in.readUTF();
				BanApi.permMute(player, reason, mutedby);
				return;
			}
			if (task.equals("TempMute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String mutedby = in.readUTF();
				Long seconds = in.readLong();
				BanApi.tempMute(player, reason, mutedby, seconds);
				return;
			}
			if (task.equals("kick")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String kickedby = in.readUTF();
				BanApi.kick(player, reason, kickedby);
				return;
			}
			if (task.equals("unban")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String unbannedby = in.readUTF();
				BanApi.unBan(player, reason, unbannedby);
				return;
			}
			if (task.equals("unmute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String unmutedby = in.readUTF();
				BanApi.unMute(player, reason, unmutedby);
				return;
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
