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

package de.linzn.mineSuite.bungee.module.ban;

import de.linzn.mineSuite.bungee.database.DataHashTable;
import de.linzn.mineSuite.bungee.database.mysql.BungeeQuery;
import de.linzn.mineSuite.bungee.module.ban.mysql.BanQuery;
import de.linzn.mineSuite.bungee.module.ban.socket.JServerBanOutput;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BanManager {
    @SuppressWarnings("deprecation")
    public static void banPlayer(final UUID uuid, final String reason, final String bannedby, final long seconds,
                                 final String pname) {
        final long current = System.currentTimeMillis();
        long end = current + seconds * 1000L;
        if (seconds == -1L) {
            end = -1L;
        }
        BanQuery.banPlayer(uuid, reason, bannedby, end);

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        String mtime = BanManager.getRemainingBanTime(end);
        if (player != null) {
            player.disconnect(getBannedMessageNew(reason, bannedby, mtime));
        }
        if (seconds == -1L) {
            JServerBanOutput.permBanMSG(pname, reason, bannedby);
        } else {
            JServerBanOutput.tempBanMSG(pname, mtime, reason, bannedby);
        }

    }

    public static boolean isBanned(ProxiedPlayer player) {
        return BanQuery.isBanned(player.getUniqueId());

    }

    public static boolean isBanned(UUID uuid) {
        return BanQuery.isBanned(uuid);

    }

    public static void unBan(final String player, final String reason, final String unbannedby) {
        UUID uuid = BungeeQuery.getUUID(player);
        if (isBanned(uuid)) {
            BanQuery.unbanPlayer(uuid, reason, unbannedby);
            JServerBanOutput.unBan(player, reason, unbannedby);
        }
    }

    public static void unBan(final UUID uuid, final String reason, final String unbannedby, final String pname) {
        if (isBanned(uuid)) {
            BanQuery.unbanPlayer(uuid, reason, unbannedby);
            JServerBanOutput.unBan(pname, reason, unbannedby);

        }
    }

    public static void unBanSystem(final UUID uuid) {
        if (isBanned(uuid)) {
            BanQuery.unbanPlayer(uuid, "Abgelaufen", "SYSTEM");

        }
    }

    public static long getEnd(final UUID uuid) {
        long end = -1L;
        end = BanQuery.getBanExpired(uuid);
        return end;
    }

    public static String getRemainingBanTime(long time) {
        String remainingTime = "";
        final long current = System.currentTimeMillis();
        final long end;
        if (time != -1L) {
            end = (time + 1000);
        } else {
            end = (time);
        }
        long difference = end - current;
        if (end == -1L) {
            return "Permanent";
        }
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int weeks = 0;
        while (difference >= 1000L) {
            difference -= 1000L;
            ++seconds;
        }
        while (seconds >= 60) {
            seconds -= 60;
            ++minutes;
        }
        while (minutes >= 60) {
            minutes -= 60;
            ++hours;
        }
        while (hours >= 24) {
            hours -= 24;
            ++days;
        }
        while (days >= 7) {
            days -= 7;
            ++weeks;
        }

        String w = "";
        String d = "";
        String h = "";
        String m = "";
        String s = "";
        if (weeks != 0) {
            w = "§e" + weeks + " Woche(n) ";
        }
        if (days != 0) {
            d = days + " Tag(e) ";
        }
        if (hours != 0) {
            h = hours + " Stunde(n) ";
        }
        if (minutes != 0) {
            m = minutes + " Minute(n) ";
        }
        if (seconds != 0) {
            s = seconds + " Sekunde(n)";
        }

        remainingTime = "§e" + w + d + h + m + s;

        return remainingTime;
    }

    public static String getBannedMessageNew(String reason, String bannedby, String time) {
        String BanMsg;
        if (time.equalsIgnoreCase("Permanent")) {
            BanMsg = "§6Du wurdest §aPermanent §6von §a" + bannedby + " §6vom Server gesperrt. \n§6Grund: §a" + reason;
        } else {
            BanMsg = "§6Du wurdest für §a" + time + " §6von §a" + bannedby + " §6vom Server gesperrt. \n§6Grund: §a"
                    + reason;
        }

        return BanMsg;
    }

    public static String getBannedMessage(UUID uuid) {
        List<String> list = BanQuery.getBanInfos(uuid);
        String reason = list.get(0);
        String bannedby = list.get(1);
        long milisec = Long.parseLong(list.get(2));
        long bannedAt = Long.parseLong(list.get(3));
        String formated = new SimpleDateFormat("dd.MM.yyyy 'um' HH:mm").format(new Date(bannedAt));
        String time = getRemainingBanTime(milisec);
        String BanMsg;
        if (time.equalsIgnoreCase("Permanent")) {
            BanMsg = "§6Du wurdest §aPermanent §6von §a" + bannedby + " §6 am " + formated
                    + " vom Server gesperrt. \n§6Grund: §a" + reason;
        } else {
            BanMsg = "§6Du wurdest für §a" + time + " §6von §a" + bannedby + " §6am " + formated
                    + " vom Server gesperrt. \n§6Grund: §a" + reason;
        }

        return BanMsg;
    }

    public static boolean isMuted(final UUID uuid) {
        if (!DataHashTable.isMuted.containsKey(uuid)) {
            if (BanQuery.isMuted(uuid)) {
                DataHashTable.isMuted.put(uuid, true);
            } else {
                DataHashTable.isMuted.put(uuid, false);
            }
        }
        return DataHashTable.isMuted.get(uuid);

    }

    public static long getMuteTime(final UUID uuid) {
        if (DataHashTable.muteTime.containsKey(uuid)) {
            if (DataHashTable.muteTime.get(uuid) != -1L) {
                return (DataHashTable.muteTime.get(uuid) + 1000);
            } else {
                return DataHashTable.muteTime.get(uuid);
            }
        }
        return -1L;

    }

    public static String getMuteReason(final UUID uuid) {
        if (DataHashTable.muteReason.containsKey(uuid)) {
            return DataHashTable.muteReason.get(uuid);
        }
        return "";

    }

    public static String getMutedBy(final UUID uuid) {
        if (DataHashTable.mutedBy.containsKey(uuid)) {
            return DataHashTable.mutedBy.get(uuid);
        }
        return "";

    }

    @SuppressWarnings("deprecation")
    public static void mutePlayer(final UUID uuid, final String reason, final String mutedby, final long seconds,
                                  final String pname) {
        final long current = System.currentTimeMillis();
        long end = current + seconds * 1000L;
        if (seconds == -1L) {
            end = -1L;
        }
        BanQuery.mutePlayer(uuid, reason, mutedby, end);
        if (!isMuted(uuid)) {
            DataHashTable.isMuted.put(uuid, true);
        }

        DataHashTable.muteTime.put(uuid, end);
        DataHashTable.muteReason.put(uuid, reason);
        DataHashTable.mutedBy.put(uuid, mutedby);

        final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
        String mtime = getRemainingMuteTime(uuid);
        if (target != null) {

            target.sendMessage(getMutedMessageNew(reason, mutedby, mtime));
        }
        if (seconds == -1L) {
            JServerBanOutput.permMuteMSG(pname, reason, mutedby);
        } else {

            JServerBanOutput.tempMuteMSG(pname, mtime, reason, mutedby);
        }

    }

    public static void unMute(final String player, final String reason, final String unmutedby) {
        UUID uuid = BungeeQuery.getUUID(player);
        if (isMuted(uuid)) {
            BanQuery.unmutePlayer(uuid, reason, unmutedby);
            DataHashTable.isMuted.remove(uuid);
            if (DataHashTable.muteTime.containsKey(uuid)) {
                DataHashTable.muteTime.remove(uuid);
            }
            if (DataHashTable.muteReason.containsKey(uuid)) {
                DataHashTable.muteReason.remove(uuid);
            }
            if (DataHashTable.mutedBy.containsKey(uuid)) {
                DataHashTable.mutedBy.remove(uuid);
            }
            JServerBanOutput.unMute(player, reason, unmutedby);

        }
    }

    public static void unMute(final UUID uuid, final String reason, final String unmutedby, final String pname) {
        if (isMuted(uuid)) {
            BanQuery.unmutePlayer(uuid, reason, unmutedby);
            DataHashTable.isMuted.remove(uuid);
            if (DataHashTable.muteTime.containsKey(uuid)) {
                DataHashTable.muteTime.remove(uuid);
            }
            if (DataHashTable.muteReason.containsKey(uuid)) {
                DataHashTable.muteReason.remove(uuid);
            }
            if (DataHashTable.mutedBy.containsKey(uuid)) {
                DataHashTable.mutedBy.remove(uuid);
            }
            JServerBanOutput.unMute(pname, reason, unmutedby);

        }
    }

    public static void unMuteSystem(final UUID uuid) {
        if (isMuted(uuid)) {
            BanQuery.unmutePlayer(uuid, "Abgelaufen", "SYSTEM");
            DataHashTable.isMuted.remove(uuid);
            if (DataHashTable.muteTime.containsKey(uuid)) {
                DataHashTable.muteTime.remove(uuid);
            }
            if (DataHashTable.muteReason.containsKey(uuid)) {
                DataHashTable.muteReason.remove(uuid);
            }
            if (DataHashTable.mutedBy.containsKey(uuid)) {
                DataHashTable.mutedBy.remove(uuid);
            }

        }
    }

    public static String getRemainingMuteTime(final UUID uuid) {
        String remainingTime = "";
        final long current = System.currentTimeMillis();
        final long end = getMuteTime(uuid);
        long difference = end - current;
        if (end == -1L) {
            return "Permanent";
        }
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int weeks = 0;
        while (difference >= 1000L) {
            difference -= 1000L;
            ++seconds;
        }
        while (seconds >= 60) {
            seconds -= 60;
            ++minutes;
        }
        while (minutes >= 60) {
            minutes -= 60;
            ++hours;
        }
        while (hours >= 24) {
            hours -= 24;
            ++days;
        }
        while (days >= 7) {
            days -= 7;
            ++weeks;
        }

        String w = "";
        String d = "";
        String h = "";
        String m = "";
        String s = "";
        if (weeks != 0) {
            w = "§e" + weeks + " Woche(n) ";
        }
        if (days != 0) {
            d = days + " Tag(e) ";
        }
        if (hours != 0) {
            h = hours + " Stunde(n) ";
        }
        if (minutes != 0) {
            m = minutes + " Minute(n) ";
        }
        if (seconds != 0) {
            s = seconds + " Sekunde(n)";
        }

        remainingTime = "§e" + w + d + h + m + s;

        return remainingTime;
    }

    public static String getMutedMessageNew(String reason, String mutedby, String time) {
        String MuteMsg;
        if (time.equalsIgnoreCase("Permanent")) {
            MuteMsg = "§6Du wurdest §aPermanent §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a"
                    + reason;
        } else {
            MuteMsg = "§6Du wurdest für §a" + time + " §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a"
                    + reason;
        }
        return MuteMsg;
    }

    public static String getMutedMessage(UUID uuid) {
        String MuteMsg;
        String time = getRemainingMuteTime(uuid);
        String mutedby = getMutedBy(uuid);
        String reason = getMuteReason(uuid);
        if (time.equalsIgnoreCase("Permanent")) {
            MuteMsg = "§6Du bist §aPermanent §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a" + reason;
        } else {
            MuteMsg = "§6Du bist für §a" + time + " §6von §a" + mutedby + " §6vom Chat ausgeschlossen. \n§6Grund: §a"
                    + reason;
        }
        return MuteMsg;
    }
}
