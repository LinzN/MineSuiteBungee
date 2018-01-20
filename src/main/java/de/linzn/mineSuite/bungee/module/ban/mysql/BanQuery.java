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

package de.linzn.mineSuite.bungee.module.ban.mysql;

import de.linzn.mineSuite.bungee.database.mysql.setup.MySQLConnectionManager;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BanQuery {

    public static void banPlayer(UUID uuid, String reason, String bannedby, long extime) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT Banned FROM bans WHERE UUID = '" + uuid.toString() + "' AND Banned = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                updateBannedPlayer(uuid, reason, bannedby, extime);
            } else {
                newBannedPlayer(uuid, reason, bannedby, extime);
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mutePlayer(UUID uuid, String reason, String bannedby, long extime) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT Muted FROM mutes WHERE UUID = '" + uuid.toString() + "' AND Muted = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                updateMutedPlayer(uuid, reason, bannedby, extime);
            } else {
                newMutedPlayer(uuid, reason, bannedby, extime);
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unbanPlayer(UUID uuid, String reason, String unbannedby) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT Banned FROM bans WHERE UUID = '" + uuid.toString() + "' AND Banned = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                updateUnbannedPlayer(uuid, reason, unbannedby);
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unmutePlayer(UUID uuid, String reason, String unmutedby) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;

        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT Muted FROM mutes WHERE UUID = '" + uuid.toString() + "' AND Muted = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                updateUnmutedPlayer(uuid, reason, unmutedby);
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void newBannedPlayer(UUID uuid, String reason, String bannedby, long extime) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        Date date = new Date();
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "INSERT INTO bans (UUID, Banned, Reason, BannedBy, ExpireTime, BannedAt) VALUES ('"
                            + uuid.toString() + "', '" + 1 + "', '" + reason.replace("'", "") + "', '" + bannedby
                            + "', '" + extime + "', '" + date.getTime() + "');");
            sql.executeUpdate();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void newMutedPlayer(UUID uuid, String reason, String mutedby, long extime) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        Date date = new Date();
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn
                    .prepareStatement("INSERT INTO mutes (UUID, Muted, Reason, MutedBy, ExpireTime, MutedAt) VALUES ('"
                            + uuid.toString() + "', '" + 1 + "', '" + reason.replace("'", "") + "', '" + mutedby
                            + "', '" + extime + "', '" + date.getTime() + "');");
            sql.executeUpdate();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBannedPlayer(UUID uuid, String reason, String bannedby, long extime) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        Date date = new Date();

        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn
                    .prepareStatement("UPDATE bans SET Banned = '" + 1 + "', Reason =  '" + reason.replace("'", "")
                            + "', BannedBy =  '" + bannedby + "', ExpireTime =  '" + extime + "', BannedAt =  '"
                            + date.getTime() + "' WHERE UUID = '" + uuid.toString() + "' AND Banned = '" + 1 + "';");
            sql.executeUpdate();
            sql.close();
            manager.release("MineSuiteBan", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMutedPlayer(UUID uuid, String reason, String mutedby, long extime) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        Date date = new Date();
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn
                    .prepareStatement("UPDATE mutes SET Muted = '" + 1 + "', Reason =  '" + reason.replace("'", "")
                            + "', MutedBy =  '" + mutedby + "', ExpireTime =  '" + extime + "', MutedAt =  '"
                            + date.getTime() + "' WHERE UUID = '" + uuid.toString() + "' AND Muted = '" + 1 + "';");
            sql.executeUpdate();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUnbannedPlayer(UUID uuid, String reason, String unbannedby) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement("UPDATE bans SET Banned = '" + 0 + "', UnBannedBy = '"
                    + unbannedby + "', UnBannedReason = '" + reason.replace("'", "") + "' WHERE UUID = '"
                    + uuid.toString() + "' AND Banned = '" + 1 + "';");
            sql.executeUpdate();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUnmutedPlayer(UUID uuid, String reason, String unmutedby) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement("UPDATE mutes SET Muted = '" + 0 + "', UnMutedBy = '"
                    + unmutedby + "', UnMutedReason = '" + reason.replace("'", "") + "' WHERE UUID = '"
                    + uuid.toString() + "' AND Muted = '" + 1 + "';");
            sql.executeUpdate();
            sql.close();
            manager.release("MineSuiteBan", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned(UUID uuid) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        Boolean banned = false;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT Banned FROM bans WHERE UUID = '" + uuid.toString() + "' AND Banned = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                banned = true;
            } else {
                banned = false;
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banned;
    }

    public static boolean isMuted(UUID uuid) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        Boolean muted = false;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT Muted FROM mutes WHERE UUID = '" + uuid.toString() + "' AND Muted = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                muted = true;
            } else {
                muted = false;
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return muted;
    }

    public static long getBanExpired(UUID uuid) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        long banned = 0;
        try {
            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT ExpireTime FROM bans WHERE UUID = '" + uuid.toString() + "' AND Banned = '" + 1 + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                banned = result.getLong(1);
            }
            result.close();
            sql.close();
            manager.release("MineSuiteBan", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banned;
    }


    public static List<String> getBanInfos(UUID uuid) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sel = conn.prepareStatement(
                    "SELECT * FROM bans WHERE UUID = '" + uuid.toString() + "' AND Banned = '" + 1 + "';");
            List<String> list = new ArrayList<String>();

            ResultSet result = sel.executeQuery();
            if (result != null) {
                @SuppressWarnings("unused")
                int i = 0;
                if (result.next()) {
                    list.add(0, result.getString("Reason"));
                    list.add(1, result.getString("BannedBy"));
                    list.add(2, result.getString("ExpireTime"));
                    list.add(3, result.getString("BannedAt"));
                }
            }
            result.close();
            sel.close();
            manager.release("MineSuiteBan", conn);

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getMuteInfos(UUID uuid) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sel = conn.prepareStatement(
                    "SELECT * FROM mutes WHERE UUID = '" + uuid.toString() + "' AND Muted = '" + 1 + "';");
            List<String> list = new ArrayList<String>();
            try {
                ResultSet result = sel.executeQuery();
                if (result != null) {
                    @SuppressWarnings("unused")
                    int i = 0;
                    if (result.next()) {
                        list.add(0, result.getString("Reason"));
                        list.add(1, result.getString("MutedBy"));
                        list.add(2, result.getString("ExpireTime"));
                        list.add(3, result.getString("BannedAt"));
                    }
                }
                result.close();
                sel.close();
                manager.release("MineSuiteBan", conn);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean clearOldBans() {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sel = conn
                    .prepareStatement("SELECT * FROM bans WHERE Banned = '" + 1 + "' AND ExpireTime > '" + -1 + "';");
            ResultSet result = sel.executeQuery();
            if (result != null) {
                while (result.next()) {
                    Long current = System.currentTimeMillis();
                    UUID uuid = UUID.fromString(result.getString("UUID"));
                    long time = result.getLong("ExpireTime");

                    if (time < current) {
                        updateUnbannedPlayer(uuid, "Abgelaufen", "SYSTEM");
                        ProxyServer.getInstance().getConsole()
                                .sendMessage("Auto-Unban " + uuid + " Grund: Abgelaufen UnbannedBy: SYSTEM");
                    }

                }
            }
            result.close();
            sel.close();
            manager.release("MineSuiteBan", conn);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean clearOldMuted() {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("MineSuiteBan");
            PreparedStatement sel = conn
                    .prepareStatement("SELECT * FROM mutes WHERE Muted = '" + 1 + "' AND ExpireTime > '" + -1 + "';");
            ResultSet result = sel.executeQuery();
            if (result != null) {
                while (result.next()) {
                    Long current = System.currentTimeMillis();
                    UUID uuid = UUID.fromString(result.getString("UUID"));
                    long time = result.getLong("ExpireTime");

                    if (time < current) {
                        updateUnmutedPlayer(uuid, "Abgelaufen", "SYSTEM");
                        ProxyServer.getInstance().getConsole()
                                .sendMessage("Auto-Unmute " + uuid + " Grund: Abgelaufen UnmutedBy: SYSTEM");
                    }

                }
            }
            result.close();
            sel.close();
            manager.release("MineSuiteBan", conn);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
