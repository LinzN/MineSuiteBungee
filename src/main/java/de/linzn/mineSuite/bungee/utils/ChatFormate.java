/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.bungee.utils;

public class ChatFormate {
    private static String guildchat = "§a[GC]§r {player}: §a{text}";
    private static String channelglobalformate = "§2[G] §f{prefix}{player}§2{suffix}: {text}";
    private static String channeltradeformate = "§9[HA] §7{player}§9: {text}";
    private static String channelstaffformate = "§4[TC] §f{player}§e: {text}";
    private static String privatemsgformate = "{player} => {reciever}: {text}";
    private static String privatemsgsenderformate = "§6Du§8 => §6{reciever}§8: §7{text}";
    private static String privatemsgrecieverformate = "§6{player}§8 => §6Dir§8: §7{text}";

    private static String broadcast = "{text}";

    public static String genGuildChat(String sender, String text) {
        String formate = guildchat.replace("{player}", sender).replace("{text}", text);
        return formate;
    }

    public static String genGlobalChat(String sender, String text, String prefix, String suffix) {
        String formate = channelglobalformate.replace("{prefix}", prefix).replace("{suffix}", suffix)
                .replace("{player}", sender).replace("{text}", text);
        return formate;
    }

    public static String genBroadcastChat(String text) {
        String formate = broadcast.replace("{text}", text);
        return formate;
    }

    public static String genTradeChat(String sender, String text, String prefix, String suffix) {
        String formate = channeltradeformate.replace("{prefix}", prefix).replace("{suffix}", suffix)
                .replace("{player}", sender).replace("{text}", text);
        return formate;
    }

    public static String genStaffChat(String sender, String text, String prefix) {
        String formate = channelstaffformate.replace("{player}", sender).replace("{text}", text);
        return formate;
    }

    public static String genPrivateSender(String sender, String reciever, String text, String prefix) {
        String formate = privatemsgsenderformate.replace("{prefix}", prefix).replace("{player}", sender)
                .replace("{reciever}", reciever).replace("{text}", text);
        return formate;
    }

    public static String genPrivateReceiver(String sender, String reciever, String text, String prefix) {
        String formate = privatemsgrecieverformate.replace("{prefix}", prefix).replace("{player}", sender)
                .replace("{reciever}", reciever).replace("{text}", text);
        return formate;
    }

    public static String genPrivate(String sender, String reciever, String text) {
        String formate = privatemsgformate.replace("{player}", sender).replace("{reciever}", reciever).replace("{text}",
                text);
        return formate;
    }
}
