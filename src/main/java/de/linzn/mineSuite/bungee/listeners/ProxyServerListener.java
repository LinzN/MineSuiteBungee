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

package de.linzn.mineSuite.bungee.listeners;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.core.Config;
import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.database.mysql.BungeeQuery;
import de.linzn.mineSuite.bungee.module.ban.BanManager;
import de.linzn.mineSuite.bungee.module.core.BungeeManager;
import de.linzn.mineSuite.bungee.module.core.socket.JServerBungeeOutput;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ProxyServerListener implements Listener {


    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW)
    public void playerLogin(PostLoginEvent event) {
        if (!DataHashTable.session.containsKey(event.getPlayer().getUniqueId())) {
            ProxyServer.getInstance()
                    .broadcast(ChatColor.GOLD + event.getPlayer().getName() + " ist " + ChatColor.GREEN + "online");
        }
        BungeeManager.initPlayer(event.getPlayer());
        event.getPlayer().sendMessage(MessageDB.default_SERVER_MODT1);
        event.getPlayer().sendMessage(MessageDB.default_SERVER_MODT2);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerLogout(final PlayerDisconnectEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        ProxyServer.getInstance().getScheduler().schedule(MineSuiteBungeePlugin.getInstance(), () -> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
            if (player == null) {
                BungeeManager.deinitPlayer(event.getPlayer());
                ProxyServer.getInstance().broadcast(
                        ChatColor.GOLD + event.getPlayer().getName() + " ist " + ChatColor.DARK_RED + "offline");
            } else {
                ProxyServer.getInstance().getLogger().warning(ChatColor.YELLOW
                        + "Spieler ist nicht richtig ausgeloggt? Hm kann passieren.");
            }
        }, 1, TimeUnit.SECONDS);
    }

    @EventHandler(priority = 64)
    public void onLogin(final LoginEvent e) {
        if (!isProtocolAllowed(e.getConnection().getVersion())) {
            e.setCancelled(true);
            e.setCancelReason("Deine Minecraft Version wird nicht unterstützt. Bitte benutze Version " + Config.getString("login.recommendedVersion"));
            MineSuiteBungeePlugin.getInstance().getLogger()
                    .info("Connection " + e.getConnection().getName() + " is using wrong protocol version: " + e.getConnection().getVersion());

        } else {
            MineSuiteBungeePlugin.getInstance().getLogger()
                    .info("Connection " + e.getConnection().getName() + " = Protocol version: " + e.getConnection().getVersion());
        }

        if (BanManager.isBanned(e.getConnection().getUniqueId())) {
            final Long current = System.currentTimeMillis();
            final Long end = BanManager.getEnd(e.getConnection().getUniqueId());
            if (end < current && end != -1L) {
                e.setCancelled(false);
                BanManager.unBanSystem(e.getConnection().getUniqueId());
            } else {
                e.setCancelled(true);
                e.setCancelReason(BanManager.getBannedMessage(e.getConnection().getUniqueId()));
            }
        }

        long timeStamp = new Date().getTime();
        String ip = e.getConnection().getAddress().getAddress().getHostAddress();
        if (BungeeQuery.updateProfile(e.getConnection().getUniqueId(), e.getConnection().getName(), timeStamp, ip)) {
            MineSuiteBungeePlugin.getInstance().getLogger()
                    .info("UUID-cache updated for incoming connection " + e.getConnection().getName() + " Address: " + ip);
        } else {
            MineSuiteBungeePlugin.getInstance().getLogger().info(
                    "FAIL! UUID-cache update for incoming connection " + e.getConnection().getName() + " failed! Address: " + ip);
        }

    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(final ChatEvent e) {
        final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        if (!BanManager.isMuted(p.getUniqueId())) {
            return;
        }
        final long current = System.currentTimeMillis();
        final long end = BanManager.getMuteTime(p.getUniqueId());
        if (end < current && end != -1L) {
            e.setCancelled(false);
            BanManager.unMuteSystem(p.getUniqueId());
            return;
        }
        if (e.isCommand()) {
            final String command = e.getMessage().replaceAll("/", "").split(" ")[0].toLowerCase();
            boolean contains = false;
            for (final String string : Config.getStringList("cmd.forbidden")) {
                if (command.equals(string.toLowerCase())) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return;
            }
        }

        e.setCancelled(true);
        e.setMessage("g");
        p.sendMessage(BanManager.getMutedMessage(p.getUniqueId()));
        return;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent ev) {
        String partialPlayerName = ev.getCursor().toLowerCase();

        int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
        if (lastSpaceIndex >= 0) {
            partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
        }

        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.getName().toLowerCase().startsWith(partialPlayerName)) {
                ev.getSuggestions().add(p.getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerKickLogin(ServerKickEvent event) {
        if (event.getState() == ServerKickEvent.State.CONNECTING && event.getPlayer().getServer() != null) {
            event.getPlayer().sendMessage(MessageDB.teleport_SERVER_ERROR);
            JServerBungeeOutput.cancelTeleport(event.getPlayer().getServer().getInfo().getName(), event.getPlayer().getUniqueId(), event.getKickedFrom().getName());
        }
    }

    private boolean isProtocolAllowed(int version) {
        List<Integer> list = Config.ConfigConfiguration.getIntList("login.allowedVersions");
        return list.contains(version);
    }

    private int getMaxSupportedVersion() {
        List<Integer> list = Config.ConfigConfiguration.getIntList("login.allowedVersions");
        int version = 0;
        for (int v : list) {
            if (v > version) {
                version = v;
            }
        }
        return version;
    }

    @EventHandler
    public void proxyPingResponse(ProxyPingEvent ev) {
        int clientVersion = ev.getConnection().getVersion();
        ServerPing ping = ev.getResponse();
        if (isProtocolAllowed(clientVersion)) {
            ping.getVersion().setProtocol(clientVersion);
        } else {
            ping.getVersion().setProtocol(this.getMaxSupportedVersion());
        }
        ping.getVersion().setName("MG-" + Config.getString("login.recommendedVersion"));
    }

}
