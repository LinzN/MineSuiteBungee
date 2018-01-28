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

package de.linzn.mineSuite.bungee;

import de.linzn.mineSuite.bungee.core.AutoBroadcaster;
import de.linzn.mineSuite.bungee.core.Config;
import de.linzn.mineSuite.bungee.core.commands.HelpCommand;
import de.linzn.mineSuite.bungee.core.commands.VoteCommand;
import de.linzn.mineSuite.bungee.core.socket.MineJSocketServer;
import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionSetup;
import de.linzn.mineSuite.bungee.listeners.ProxyServerListener;
import de.linzn.mineSuite.bungee.module.ban.AutoUnbanChecker;
import de.linzn.mineSuite.bungee.module.chat.VoteInformer;
import de.linzn.mineSuite.bungee.module.portal.PortalManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class MineSuiteBungeePlugin extends Plugin {

    private static MineSuiteBungeePlugin instance;
    public ProxyServer proxy;

    private MineJSocketServer mineJSocketServer;

    public static MineSuiteBungeePlugin getInstance() {
        return instance;
    }

    @Override

    public void onEnable() {
        instance = this;
        this.proxy = ProxyServer.getInstance();
        this.proxy.getConsole().sendMessage(ChatColor.BLUE + "Loading MineSuite...");
        this.getProxy().getPluginManager().registerCommand(this, new HelpCommand());
        this.getProxy().getPluginManager().registerCommand(this, new VoteCommand());
        Config fileManager = new Config(this);
        fileManager.setDefaultConfig();
        if (MySQLConnectionSetup.create()) {
            this.registerListeners();
            this.startScheduler();
            this.proxy.getConsole().sendMessage(ChatColor.BLUE + "Finish!");
            this.mineJSocketServer = new MineJSocketServer();
            this.mineJSocketServer.openServer();
            PortalManager.loadPortalsToHash();
        }
    }

    @Override
    public void onDisable() {
        this.mineJSocketServer.closeServer();
    }


    public MineJSocketServer getMineJSocketServer() {
        return mineJSocketServer;
    }

    private void registerListeners() {
        this.proxy.getPluginManager().registerListener(this, new ProxyServerListener());
    }


    private void startScheduler() {
        this.proxy.getScheduler().schedule(MineSuiteBungeePlugin.instance, new AutoUnbanChecker(), 2, 4, TimeUnit.MINUTES);
        int time = Config.getInt("broadcaster.time");
        this.proxy.getScheduler().schedule(MineSuiteBungeePlugin.instance, new AutoBroadcaster(), 60, time, TimeUnit.SECONDS);
        this.proxy.getScheduler().schedule(MineSuiteBungeePlugin.instance, new VoteInformer(), 20, 300, TimeUnit.SECONDS);
        this.proxy.getConsole().sendMessage(ChatColor.BLUE + "Scheduler enabled!");
    }

}