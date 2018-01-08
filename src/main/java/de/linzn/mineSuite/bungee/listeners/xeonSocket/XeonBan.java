package de.linzn.mineSuite.bungee.listeners.xeonSocket;

import de.linzn.mineSuite.bungee.BanApi;
import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.javaSocket.server.events.SocketDataEvent;
import de.nlinz.javaSocket.server.interfaces.IDataListener;

import java.io.DataInputStream;
import java.io.IOException;

public class XeonBan implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonBan";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = XeonSocketServerManager.readDataInput(event.getStreamBytes());
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
