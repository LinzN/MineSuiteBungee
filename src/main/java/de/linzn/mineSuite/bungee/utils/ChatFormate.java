package de.linzn.mineSuite.bungee.utils;

public class ChatFormate {
	private static String guildchat = "§6[§aGilde§6] {player}: §a{text}";
	private static String channelglobalformate = "§2[G] §f{prefix}{player}§2{suffix}: {text}";
	private static String channelstaffformate = "§6[§4Team§6] §f{player}§e: {text}";
	private static String privatemsgformate = "{player} -> {reciever}: {text}";
    private static String privatemsgsenderformate = "§6Du§8 -> §6{reciever}§8: §7{text}";
    private static String privatemsgrecieverformate = "§6{player}§8 -> §6Dir§8: §7{text}";

	public static String toGuildFormate(String sender, String text) {
		String formate = guildchat.replace("{player}", sender).replace("{text}", text);
		return formate;
	}

	public static String toChannelGlobalFormate(String sender, String text, String prefix, String suffix) {
		String formate = channelglobalformate.replace("{prefix}", prefix).replace("{suffix}", suffix)
				.replace("{player}", sender).replace("{text}", text);
		return formate;
	}

	public static String toChannelStaffFormate(String sender, String text, String prefix) {
		String formate = channelstaffformate.replace("{player}", sender).replace("{text}", text);
		return formate;
	}

	public static String toPrivateMsgSenderFormate(String sender, String reciever, String text, String prefix) {
		String formate = privatemsgsenderformate.replace("{prefix}", prefix).replace("{player}", sender)
				.replace("{reciever}", reciever).replace("{text}", text);
		return formate;
	}

	public static String toPrivateMsgRecieverFormate(String sender, String reciever, String text, String prefix) {
		String formate = privatemsgrecieverformate.replace("{prefix}", prefix).replace("{player}", sender)
				.replace("{reciever}", reciever).replace("{text}", text);
		return formate;
	}

	public static String toPrivateMsgFormate(String sender, String reciever, String text) {
		String formate = privatemsgformate.replace("{player}", sender).replace("{reciever}", reciever).replace("{text}",
				text);
		return formate;
	}
}
