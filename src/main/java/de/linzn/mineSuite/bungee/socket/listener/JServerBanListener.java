package de.linzn.mineSuite.bungee.socket.listener;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.BanApi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JServerBanListener implements IncomingDataListener {

	@Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
		String subChannel;
		try {
            subChannel = in.readUTF();

			if (subChannel.equals("ban_perma-ban")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String bannedby = in.readUTF();
				BanApi.permBan(player, reason, bannedby);

				return;
			}
			if (subChannel.equals("ban_temp-ban")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String bannedby = in.readUTF();
				Long seconds = in.readLong();
				BanApi.tempBan(player, reason, bannedby, seconds);
				return;
			}
			if (subChannel.equals("ban_perma-mute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String mutedby = in.readUTF();
				BanApi.permMute(player, reason, mutedby);
				return;
			}
			if (subChannel.equals("ban_temp-mute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String mutedby = in.readUTF();
				Long seconds = in.readLong();
				BanApi.tempMute(player, reason, mutedby, seconds);
				return;
			}
			if (subChannel.equals("ban_kick")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String kickedby = in.readUTF();
				BanApi.kick(player, reason, kickedby);
				return;
			}
			if (subChannel.equals("ban_unban")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String unbannedby = in.readUTF();
				BanApi.unBan(player, reason, unbannedby);
				return;
			}
			if (subChannel.equals("ban_unmute")) {
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
