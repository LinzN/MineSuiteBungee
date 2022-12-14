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
    public static String default_SERVER_MODT1 = ChatColor.DARK_RED + "" + ChatColor.BOLD + "■■■ " + ChatColor.GREEN + "Willkommen auf MineGaming.de - Errichte deine Welt" + ChatColor.DARK_RED + "" + ChatColor.BOLD + " ■■■";
    public static String default_SERVER_MODT2 = ChatColor.GREEN + "" + ChatColor.BOLD + "Hilfesystem: " + ChatColor.DARK_PURPLE + "/help" + "" + ChatColor.GREEN + "" + ChatColor.BOLD + " ■ ■ ■ ■ ■ ■ Forum: " + ChatColor.DARK_PURPLE + "www.MineGaming.de";

    public static String default_PLAYER_ENTRY_NOT_EXIST = ChatColor.RED + "" + ChatColor.BOLD + "Der Spieler {player} existiert nicht oder hat für diesen Wert keinen Eintrag!";
    public static String default_PLAYER_NOT_EXIST = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Spieler wurde noch nie gesehen. Bist du dir bei dem Namen sicher?";
    public static String default_PLAYER_NOT_ONLINE = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Spieler ist nicht online!";
    public static String default_PLAYER_IP_CHECK = ChatColor.GREEN + "IP check ist: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "{value}";
    public static String default_PLAYER_LAST_SEEN = ChatColor.GREEN + "Spieler {player} zuletzt gesehen: " + ChatColor.YELLOW + "" + ChatColor.BOLD + "{date}";
    public static String default_TRY_TO_TELEPORT = ChatColor.GREEN + "" + ChatColor.BOLD + "Versuche zu teleportieren...";
    public static String default_NO_ACCESS = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Keine Berechtigung!! Nur Shell benutzer!!!";
    public static String default_RELOADED = ChatColor.BLUE + "" + ChatColor.BOLD + "Konfiguration neu geladen!";
    public static String default_LIST_MOREPAGE = ChatColor.GREEN + "" + ChatColor.BOLD + "Mehr auf Seite " + ChatColor.YELLOW + "{page}" + ChatColor.GREEN + "" + ChatColor.BOLD + " mit " + ChatColor.YELLOW + "{command} " + "{page}";
    public static String default_LIST_BACKPAGE = ChatColor.GREEN + "" + ChatColor.BOLD + "Zurück auf Seite " + ChatColor.YELLOW + "{page}" + ChatColor.GREEN + "" + ChatColor.BOLD + " mit " + ChatColor.YELLOW + "{command} " + "{page}";


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
    public static String teleport_PLAYER_REQUESTS_YOU_TELEPORT_TO_THEM = ChatColor.YELLOW + "" + ChatColor.BOLD + "{player} möchte dich zu sich einladen. " + "\n" + ChatColor.YELLOW + "" + ChatColor.BOLD + "Annehmen: /tpaccept Ablehnen: /tpdeny";
    public static String teleport_TELEPORT_ACCEPTED = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast die Einladung von {player} akzeptiert!";
    public static String teleport_TELEPORT_REQUEST_ACCEPTED = ChatColor.GREEN + "" + ChatColor.BOLD + "{player} hat deine Einladung angenommen!";
    public static String teleport_TELEPORT_DENIED = ChatColor.YELLOW + "" + ChatColor.BOLD + "Du hast die Einladung von {player} abgelehnt!!";
    public static String teleport_TELEPORT_REQUEST_DENIED = ChatColor.RED + "" + ChatColor.BOLD + "{player} hat deine Einladung abgelehnt!";
    public static String teleport_NO_TELEPORTS = ChatColor.RED + "" + ChatColor.BOLD + "Du hast keine offenen Einladungen!";
    public static String teleport_TELEPORT_REQUEST_SENT = ChatColor.YELLOW + "" + ChatColor.BOLD + "Deine Einladung wurde versendet!";
    public static String teleport_TPA_REQUEST_TIMED_OUT = ChatColor.RED + "" + ChatColor.BOLD + "Die Einlandung an {player} ist abgelaufen!";
    public static String teleport_TP_REQUEST_OTHER_TIMED_OUT = ChatColor.RED + "" + ChatColor.BOLD + "Die Einladung von {player} ist abgelaufen!";
    public static String teleport_TPAHERE_REQUEST_TIMED_OUT = ChatColor.RED + "" + ChatColor.BOLD + "Die Einladung an {player} ist abgelaufen!";
    public static String teleport_NO_BACK_TP = ChatColor.RED + "" + ChatColor.BOLD + "Es wurde kein Punkt gefunden, der dich zurückbringt!";
    public static String teleport_TELEPORT_UNABLE = ChatColor.RED + "" + ChatColor.BOLD + "Teleport mit diesem Spieler ist nicht möglich!";
    public static String teleport_CONFIRM_ERROR = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Error on CONFIRM task. Please contact a team member!";
    public static String teleport_SERVER_ERROR = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Error on SERVER task. Please contact a team member!";

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
    public static String home_HOME_PAGE_NO_HOMES = ChatColor.RED + "" + ChatColor.BOLD + "Es wurden hier keine Home-Punkte gefunden!";
    public static String home_HOME_PAGE_HOMES = ChatColor.GREEN + "" + ChatColor.BOLD + "Auflistung deiner Home-Punkte: ";
    public static String home_HOME_PAGE_ENTRY = ChatColor.GREEN + "" + ChatColor.BOLD + "Home-Punkt: " + ChatColor.YELLOW + "{home}" + ChatColor.GREEN + "" + ChatColor.BOLD + " Server: " + ChatColor.YELLOW + "{server}";


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
    public static String chat_INFORM_VOTER = "Danke dass du für uns gevotet hat. Hier deine Belohnung! {value} Mines :D";
    public static String chat_NO_VOTER = "Leider hat niemand gevotet :(! Jetzt voten und bis zu 450 Mines verdienen!";
    public static String chat_HAS_VOTER = "Die Spieler " + "{playernames}" + " haben auf https://Vote.MineGaming.de gevotet und wurden mit bis zu 450 Mines belohnt!";
    public static String chat_HAS_VOTER_SINGLE = "Der Spieler " + "{player}" + " hat auf https://Vote.MineGaming.de gevotet und wurde mit bis zu 450 Mines belohnt!";
    public static String chat_HAS_VOTER_VOTE_LINK = "https://vote.minegaming.de";
    public static String chat_HAS_VOTER_VOTE_HOVER = "Jetzt auf Vote.MineGaming.de voten :D";

    /* Warp Values */
    public static String warp_NO_WARP = ChatColor.RED + "" + ChatColor.BOLD + "Dieser Warppunkt existiert nicht.";
    public static String warp_REFRESH_WARP = ChatColor.GREEN + "" + ChatColor.BOLD + "Du den Warppunkt {warp} aktualisiert!";
    public static String warp_NEW_WARP = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den Warppunkt {warp} gesetzt!";
    public static String warp_DELETE_WARP = ChatColor.GREEN + "" + ChatColor.BOLD + "Du hast den Warppunkt {warp} entfernt!";
    public static String warp_WARP_PAGE_NO_WARPS = ChatColor.RED + "" + ChatColor.BOLD + "Es wurden hier keine Warp-Punkte gefunden!";
    public static String warp_WARP_PAGE_WARPS = ChatColor.GREEN + "" + ChatColor.BOLD + "Auflistung der Server Warp-Punkte ";
    public static String warp_WARP_PAGE_ENTRY = ChatColor.GREEN + "" + ChatColor.BOLD + "Warp-Punkt: " + ChatColor.YELLOW + "{warp}" + ChatColor.GREEN + "" + ChatColor.BOLD + " Ersteller: " + ChatColor.YELLOW + "{player}";
}
