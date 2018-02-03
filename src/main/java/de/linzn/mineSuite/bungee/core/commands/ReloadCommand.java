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

package de.linzn.mineSuite.bungee.core.commands;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import de.linzn.mineSuite.bungee.utils.MessageDB;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("msreload");
    }

    public void execute(CommandSender sender, String[] args) {
        this.vote(sender);
    }

    private void vote(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(MessageDB.default_NO_ACCESS);
            return;
        }
        MineSuiteBungeePlugin.getInstance().fileManager.reloadConfig();
        sender.sendMessage(MessageDB.default_RELOADED);
    }
}
