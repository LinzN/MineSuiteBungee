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

package de.linzn.mineSuite.bungee.module.chat.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatQuery {


    public static List<String[]> getMailList(UUID playerUUID) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteChat");
            PreparedStatement sel = conn.prepareStatement(
                    "SELECT * FROM module_chat_mails WHERE UUID = '" + playerUUID.toString() + "';");
            List<String[]> list = new ArrayList<>();

            ResultSet result = sel.executeQuery();
            while (result.next()) {
                int mailID = result.getInt("id");
                UUID receiverUUID = UUID.fromString(result.getString("reveiverUUID"));
                UUID senderUUID = UUID.fromString(result.getString("senderUUID"));
                String content = result.getString("content");
                //todo new mail object
            }
            result.close();
            sel.close();
            manager.release("MineSuiteChat", conn);

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
