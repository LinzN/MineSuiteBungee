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

package de.linzn.mineSuite.bungee.database.mysql.setup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class XeonConnectionFactory {

    private final String url;
    private final String user;
    private final String pass;

    public XeonConnectionFactory(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection create() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}