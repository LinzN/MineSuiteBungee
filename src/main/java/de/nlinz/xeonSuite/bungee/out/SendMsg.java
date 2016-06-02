package de.nlinz.xeonSuite.bungee.out;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.nlinz.javaSocket.server.api.XeonSocketServerManager;
import de.nlinz.xeonSuite.bungee.listeners.xeonSocket.XeonBan;
import net.md_5.bungee.api.ProxyServer;

@SuppressWarnings("deprecation")
public class SendMsg {

	public static void permBanMSG(String banned, String reason, String bannedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + banned + " §6wurde Permanent von §a" + bannedby
				+ " §6vom Server gesperrt.";
		String sendreason = "§6Grund: §a" + reason;
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		ProxyServer.getInstance().getConsole().sendMessage(sendreason);
		try {
			out.writeUTF("SendActionMessage");
			out.writeUTF(sendmessage);
			out.writeUTF(sendreason);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}

	public static void tempBanMSG(String banned, String time, String reason, String bannedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + banned + " §6wurde für §a" + time + " §6von §a" + bannedby
				+ " §6vom Server gesperrt.";
		String sendreason = "§6Grund: §a" + reason;
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		ProxyServer.getInstance().getConsole().sendMessage(sendreason);

		try {
			out.writeUTF("SendActionMessage");
			out.writeUTF(sendmessage);
			out.writeUTF(sendreason);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}

	public static void permMuteMSG(String muted, String reason, String mutedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + muted + " §6wurde §aPermanent §6von §a" + mutedby
				+ " §6vom Chat ausgeschlossen.";
		String sendreason = "§6Grund: §a" + reason;
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		ProxyServer.getInstance().getConsole().sendMessage(sendreason);

		try {
			out.writeUTF("SendActionMessage");
			out.writeUTF(sendmessage);
			out.writeUTF(sendreason);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}

	public static void tempMuteMSG(String muted, String time, String reason, String mutedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + muted + " §6wurde für §a" + time + " §6von §a" + mutedby
				+ " §6vom Chat ausgeschlossen.";
		String sendreason = "§6Grund: §a" + reason;
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		ProxyServer.getInstance().getConsole().sendMessage(sendreason);

		try {
			out.writeUTF("SendActionMessage");
			out.writeUTF(sendmessage);
			out.writeUTF(sendreason);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}

	public static void kickMSG(String kicked, String reason, String kickedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + kicked + " §6wurde von §a" + kickedby
				+ " §6vom Server geschmissen. \nGrund: §a" + reason;
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		try {
			out.writeUTF("SendDeActionMessage");
			out.writeUTF(sendmessage);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}

	public static void unMute(String unmuted, String reason, String unmutedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + unmuted + " §6wurde von §a" + unmutedby + " §6 zum Chat hinzugefügt.";
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		try {
			out.writeUTF("SendDeActionMessage");
			out.writeUTF(sendmessage);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}

	public static void unBan(String unbanned, String reason, String unbannedby) {
		ProxyServer.getInstance().getServers();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream out = XeonSocketServerManager.createChannel(bytes, XeonBan.channelName);
		String sendmessage = "§6Spieler §a" + unbanned + " §6wurde von §a" + unbannedby + " §6vom Server entsperrt.";
		ProxyServer.getInstance().getConsole().sendMessage(sendmessage);
		try {
			out.writeUTF("SendDeActionMessage");
			out.writeUTF(sendmessage);

		} catch (IOException e) {
			e.printStackTrace();
		}

		XeonSocketServerManager.sendData(bytes);

	}
}
