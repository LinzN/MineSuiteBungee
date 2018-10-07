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

package de.linzn.mineSuite.bungee.core;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Random;

public class AutoBroadcaster implements Runnable {


    private static void broadcast(String text, String onClick, String onHover) {
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            TextComponent vote = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
            if (!onClick.equals("none")) {
                vote.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, onClick));
            }

            if (!onHover.equals("none")) {
                vote.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(onHover)).create()));
            }
            p.sendMessage(vote);
        }
        ProxyServer.getInstance().getLogger().info(text);
    }

    public void run() {
        List<String[]> broadcastList = Config.getKeyValues("broadcaster.messages");

        int index = new Random().nextInt(broadcastList.size());

        String[] broadCast = broadcastList.get(index);

        broadcast(broadCast[0], broadCast[1], broadCast[2]);
    }

}
