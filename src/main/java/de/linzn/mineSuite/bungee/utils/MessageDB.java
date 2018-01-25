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

import net.md_5.bungee.api.ChatColor;

public class MessageDB {


    /* Default Values */
    public static String default_PLAYER_NOT_EXIST = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Spieler wurde noch nie gesehen. Bist du dir bei dem Namen sicher?";
    public static String default_PLAYER_NOT_ONLINE = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Spieler ist nicht online!";
    public static String default_TRY_TO_TELEPORT = ChatColor.GREEN + "" + ChatColor.BOLD + "Versuche zu teleportieren...";

    /* Teleport Values*/
    public static String teleport_NOT_SET_SPAWNTYPE = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Spawnpunkt konnte nicht gefunden werden :(";
    public static String teleport_NO_VALID_SPAWNTYPE = ChatColor.RED + "" + ChatColor.BOLD + "Kein gültiger SpawnType. Gültig: Lobby und ServerSpawn";
    public static String teleport_REFRESH_SPAWNTYPE = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den SpawnType {spawn} aktualisiert!";
    public static String teleport_NEW_SPAWNTYPE = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den SpawnType {spawn} gesetzt!";
    public static String teleport_DELETE_SPAWNTYPE = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den SpawnType {spawn} entfernt!";

    public static String teleport_PLAYER_TELEPORTED = ChatColor.GREEN + "" + ChatColor.BOLD + "{player} wurde zu {target} teleportiert!";
    public static String teleport_ALL_PLAYERS_TELEPORTED = ChatColor.GREEN + "" + ChatColor.BOLD + "Alle Spieler wurden zu {player} teleportiert!";
    public static String teleport_TELEPORTED_TO_PLAYER = ChatColor.GREEN + "" + ChatColor.BOLD + "Du wurdest zu {player} eingeladen!";
    public static String teleport_PLAYER_TELEPORT_PENDING = ChatColor.RED + "" + ChatColor.BOLD + "Du hast bereits eine Einladung offen!";
    public static String teleport_PLAYER_TELEPORT_PENDING_OTHER = ChatColor.RED + "" + ChatColor.BOLD + "Bei diesem Spieler ist bereits eine Einladung offen!";
    public static String teleport_PLAYER_TELEPORTED_TO_YOU = ChatColor.GREEN + "" + ChatColor.BOLD + "{player} wurde zu dir eingeladen!";
    public static String teleport_PLAYER_REQUESTS_TO_TELEPORT_TO_YOU = ChatColor.YELLOW + "" + ChatColor.BOLD + "{player} möchte dich besuchen. " + "\n" + ChatColor.YELLOW + "" + ChatColor.BOLD + "Annehmen: /tpaccept Ablehnen: /tpdeny";
    public static String teleport_PLAYER_REQUESTS_YOU_TELEPORT_TO_THEM = ChatColor.YELLOW + "" + ChatColor.BOLD + "{player} möchte dich zu sich einladen. " + ChatColor.YELLOW + "" + ChatColor.BOLD + "\n" + "Annehmen: /tpaccept Ablehnen: /tpdeny";
    public static String teleport_TELEPORT_ACCEPTED = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast die Einladung von {player} akzeptiert!";
    public static String teleport_TELEPORT_REQUEST_ACCEPTED = ChatColor.GREEN + "" + ChatColor.BOLD + "{player} hat deine Einladung angenommen!";
    public static String teleport_TELEPORT_DENIED = ChatColor.YELLOW + "" + ChatColor.BOLD + "Du hast die Einladung von {player} abhelehnt!!";
    public static String teleport_TELEPORT_REQUEST_DENIED = ChatColor.RED + "" + ChatColor.BOLD + "{player} hat deine Einladung abgelehnt!";
    public static String teleport_NO_TELEPORTS = ChatColor.RED + "" + ChatColor.BOLD + "Du hast keine offenen Einladungen!";
    public static String teleport_TELEPORT_REQUEST_SENT = ChatColor.YELLOW + "" + ChatColor.BOLD + "Deine Einladung wurde versendet!";
    public static String teleport_TPA_REQUEST_TIMED_OUT = ChatColor.RED + "" + ChatColor.BOLD + "Die Einlandung an {player} ist abgelaufen!";
    public static String teleport_TP_REQUEST_OTHER_TIMED_OUT = ChatColor.RED + "" + ChatColor.BOLD + "Die Einladung von {player} ist abgelaufen!";
    public static String teleport_TPAHERE_REQUEST_TIMED_OUT = ChatColor.RED + "" + ChatColor.BOLD + "Die Einladung an {player} ist abgelaufen!";
    public static String teleport_NO_BACK_TP = ChatColor.RED + "" + ChatColor.BOLD + "Es wurde kein Punkt gefunden, der dich zurückbringt!";
    public static String teleport_TELEPORT_UNABLE = ChatColor.RED + "" + ChatColor.BOLD + "Teleport mit diesem Spieler ist nicht möglich!";

    /* Ban Values */
    public static String ban_BANNED_PERM_NOW = ChatColor.RED + "Du wurdest Permanent von " + ChatColor.GOLD + "{bannedby}" + ChatColor.RED + " vom Server gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_BANNED_TEMP_NOW = ChatColor.RED + "Du wurdest für " + "{time}" + " von " + ChatColor.GOLD + "{bannedby}" + ChatColor.RED + " vom Server gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_MUTED_PERM_NOW = ChatColor.RED + "Du wurdest Permanent von " + ChatColor.GOLD + "{mutedby}" + ChatColor.RED + " vom Chat gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_MUTED_TEMP_NOW = ChatColor.RED + "Du wurdest für " + "{time}" + " von " + ChatColor.GOLD + "{mutedby}" + ChatColor.RED + " vom Chat gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_BANNED_PERM_INFO = ChatColor.RED + "Du wurdest Permanent von " + ChatColor.GOLD + "{bannedby}" + ChatColor.RED + " am " + "{date}" + " vom Server gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_BANNED_TEMP_INFO = ChatColor.RED + "Du wurdest für " + "{time}" + " von " + ChatColor.GOLD + "{bannedby}" + ChatColor.RED + " am " + "{date}" + " vom Server gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_MUTED_PERM_INFO = ChatColor.RED + "Du bist Permanent von " + ChatColor.GOLD + "{mutedby}" + ChatColor.RED + " vom Chat gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";
    public static String ban_MUTED_TEMP_INFO = ChatColor.RED + "Du bist für " + "{time}" + " von " + ChatColor.GOLD + "{mutedby}" + ChatColor.RED + " vom Chat gesperrt. " + "\n " + "Grund: " + ChatColor.GOLD + "{reason}";

    /* Home Values */
    public static String home_NO_HOME = ChatColor.RED + "" + ChatColor.BOLD + "Es wurde kein Homepunkt gefunden :(";
    public static String home_REFRESH_HOME = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast dein Homepunkt {home} aktualisiert!";
    public static String home_NEW_HOME = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den Homepunkt {home} gesetzt!";
    public static String home_DELETE_HOME = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den Homepunkt {home} entfernt!";
    public static String home_HOME_LIMIT = ChatColor.RED + "" + ChatColor.BOLD + "Du hast leider die maximale Anzahl deiner Homepunkte erreicht!";

    /* Portal Values */
    public static String portal_PORTAL_NO_TYPE = ChatColor.RED + "" + ChatColor.BOLD + "Das ist keine gültiger Portaltyp! Gültig: Warp oder Server";
    public static String portal_PORTAL_ERROR = ChatColor.RED + "" + ChatColor.BOLD + "Es ist ein Fehler aufgetreten. Bitte an Kekshaus wenden :)!";
    public static String portal_PORTAL_NO_PORTAL = ChatColor.RED + "" + ChatColor.BOLD + "Das Portal gibt es nicht";
    public static String portal_PORTAL_CREATED = ChatColor.GREEN + "" + ChatColor.BOLD + "Das Portal wurde erfolgreich gesetzt";
    public static String portal_PORTAL_REFRESH = ChatColor.GREEN + "" + ChatColor.BOLD + "Das Portal wurde erfolgreich aktualisiert";
    public static String portal_PORTAL_DELETED = ChatColor.GREEN + "" + ChatColor.BOLD + "Das Portal wurde erfolgreich entfernt";

    /* Chat Values */
    public static String chat_AFK_MARKED = ChatColor.YELLOW + "" + ChatColor.BOLD + "Dieser Spieler ist als abwesend makiert. Er wird vorraussichtlich nicht antworten.";
    public static String chat_NO_REPLY = ChatColor.RED + "" + ChatColor.BOLD + "Du hast niemand wo du antworten kannst :(";

    /* Portal Values */
    public static String warp_NO_WARP = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Warppunkt existiert nicht.";
    public static String warp_REFRESH_WARP = ChatColor.GREEN + "" + ChatColor.BOLD + "Du den Warppunkt {warp} aktualisiert!";
    public static String warp_NEW_WARP = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den Warppunkt {warp} gesetzt!";
    public static String warp_DELETE_WARP = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den Warppunkt {warp} entfernt!";
}
