package de.linzn.mineSuite.bungee.listeners.xeonSocket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.bungee.BanApi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class XeonBan implements IncomingDataListener {

	@Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel = null;
		try {
            subChannel = in.readUTF();

            if (subChannel.equals("PermaBan")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String bannedby = in.readUTF();
				BanApi.permBan(player, reason, bannedby);

				return;
			}
            if (subChannel.equals("TempBan")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String bannedby = in.readUTF();
				Long seconds = in.readLong();
				BanApi.tempBan(player, reason, bannedby, seconds);
				return;
			}
            if (subChannel.equals("PermaMute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String mutedby = in.readUTF();
				BanApi.permMute(player, reason, mutedby);
				return;
			}
            if (subChannel.equals("TempMute")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String mutedby = in.readUTF();
				Long seconds = in.readLong();
				BanApi.tempMute(player, reason, mutedby, seconds);
				return;
			}
            if (subChannel.equals("kick")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String kickedby = in.readUTF();
				BanApi.kick(player, reason, kickedby);
				return;
			}
            if (subChannel.equals("unban")) {
				String player = in.readUTF();
				String reason = in.readUTF();
				String unbannedby = in.readUTF();
				BanApi.unBan(player, reason, unbannedby);
				return;
			}
            if (subChannel.equals("unmute")) {
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
