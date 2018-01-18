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

public class MessageDB {

    public static String PLAYER_NOT_ONLINE = "§6" + "Dieser Spieler ist nicht online!";
    public static String ALL_PLAYERS_TELEPORTED = "§a" + "Alle Spieler wurden zu {player} teleportiert!";
    public static String TELEPORTED_TO_PLAYER = "§a" + "Du wurdest zu {player} teleportiert!";
    public static String PLAYER_TELEPORT_PENDING = "§6" + "Du hast bereits eine Anfrage offen!";
    public static String PLAYER_TELEPORT_PENDING_OTHER = "§6" + "Bei diesem Spieler ist bereits eine Anfrage offen!";
    public static String PLAYER_TELEPORTED_TO_YOU = "§a" + "{player} hat sich zu dir teleportiert!";
    public static String PLAYER_TELEPORTED = "§a" + "{player} wurde zu {target} teleportiert!";
    public static String PLAYER_REQUESTS_TO_TELEPORT_TO_YOU = "§a"
            + "{player} möchte sich zu dir teleportieren. Annehmen: /tpaccept Ablehnen: /tpdeny";
    public static String PLAYER_REQUESTS_YOU_TELEPORT_TO_THEM = "§a"
            + "{player} möchte dich zu ihm teleportieren. Annehmen: /tpaccept Ablehnen: /tpdeny";
    public static String TELEPORT_ACCEPTED = "§a" + "Du hast die Anfrage von {player}' akzeptiert!";
    public static String TELEPORT_REQUEST_ACCEPTED = "§a" + "{player} hat deine Anfrage angenommen!";
    public static String TELEPORT_DENIED = "§a" + "Du hast die Anfrage von {player}' abhelehnt!!";
    public static String TELEPORT_REQUEST_DENIED = "§6" + "{player} hat deine Anfrage abgelehnt!";
    public static String NO_TELEPORTS = "§6" + "Du hast keine offenen Anfragen!";
    public static String TELEPORT_REQUEST_SENT = "§a" + "Deine Anfrage wurde versendet!";
    public static String TPA_REQUEST_TIMED_OUT = "§6" + "Deine Anfrage an {player} ist abgelaufen!";
    public static String TP_REQUEST_OTHER_TIMED_OUT = "§6" + "Die Anfrage von {player}' ist abgelaufen!";
    public static String TPAHERE_REQUEST_TIMED_OUT = "§6" + "Deine Anfrage an {player} ist abgelaufen!";
    public static String NO_BACK_TP = "§6" + "Du hast keinen unbesuchten Todespunkt mehr!";
    public static String TELEPORT_UNABLE = "§6" + "Teleportinteraktion mit diesem Spieler ist nicht möglich!";
    public static String PLAYER_NOT_EXIST = "§6" + "Dieser Spieler war noch nie auf dem Server online!";

}
