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

package de.linzn.mineSuite.bungee.module.chat;

import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;

public class VoteInformer implements Runnable {

    private static HashSet<String> voters = new HashSet<>();

    public static void sendVoteInfoToUser(String playerName, double value) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null) {
            TextComponent vote = new TextComponent(MessageDB.chat_INFORM_VOTER.replace("{value}", "" + value));
            vote.setColor(ChatColor.GREEN);
            vote.setBold(true);
            player.sendMessage(vote);
        }
        voters.add(playerName);
    }

    public void run() {
        StringBuilder playerNames = null;
        for (String playerName : voters) {
            if (playerNames == null) {
                playerNames = new StringBuilder(playerName);
            } else {
                playerNames.append(", ").append(playerName);
            }
        }
        TextComponent vote = new TextComponent();
        if (playerNames != null) {
            if (voters.size() != 1) {
                vote.setText(ChatColor.GREEN + MessageDB.chat_HAS_VOTER.replace("{playernames}", ChatColor.GOLD + "" + ChatColor.BOLD + playerNames + ChatColor.RESET + ChatColor.GREEN));
                vote.setColor(ChatColor.GREEN);
            } else {
                vote.setText(ChatColor.GREEN + MessageDB.chat_HAS_VOTER_SINGLE.replace("{player}", ChatColor.GOLD + "" + ChatColor.BOLD + playerNames + ChatColor.RESET + ChatColor.GREEN));
                vote.setColor(ChatColor.GREEN);
            }
        } else {
            vote.setText(MessageDB.chat_NO_VOTER);
            vote.setColor(ChatColor.GOLD);
        }
        vote.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, MessageDB.chat_HAS_VOTER_VOTE_LINK));
        vote.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(MessageDB.chat_HAS_VOTER_VOTE_HOVER)).create()));
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(vote);
        }
        ProxyServer.getInstance().getLogger().info(vote.getText());
        voters.clear();
    }

}
