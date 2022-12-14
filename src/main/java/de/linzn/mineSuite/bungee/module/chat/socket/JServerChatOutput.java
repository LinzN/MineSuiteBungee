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

package de.linzn.mineSuite.bungee.module.chat.socket;

import de.linzn.mineSuite.bungee.MineSuiteBungeePlugin;
import net.md_5.bungee.api.ProxyServer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JServerChatOutput {


    public static void staffChat(String formattedText) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("server_chat_staff-chat");
            dataOutputStream.writeUTF(formattedText);

        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteChat", byteArrayOutputStream.toByteArray());
        ProxyServer.getInstance().getLogger().info("TC" + "-> " + formattedText);
    }

    public static void titleBroadcast(String title, String subTitle, int time) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("server_data-title");
            dataOutputStream.writeUTF(title);
            dataOutputStream.writeUTF(subTitle);
            dataOutputStream.writeInt(time);

        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteBungeePlugin.getInstance().getMineJSocketServer().broadcastClients("mineSuiteChat", byteArrayOutputStream.toByteArray());
        ProxyServer.getInstance().getLogger().info("Title" + "-> " + title + "->" + subTitle + "->" + time);
    }


}
