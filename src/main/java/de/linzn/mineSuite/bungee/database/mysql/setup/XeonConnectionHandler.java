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
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

public class XeonConnectionHandler {

    private final String name;
    private final XeonConnectionFactory factory;
    private final LinkedBlockingQueue<Connection> queue;

    protected XeonConnectionHandler(String name, XeonConnectionFactory factory) {
        this.name = name;
        this.factory = factory;
        this.queue = new LinkedBlockingQueue<>();
    }

    public String name() {
        return name;
    }

    public Connection getConnection() throws SQLException {
        Connection c = queue.poll();
        if (c == null) {
            c = factory.create();
        } else if (!check(c)) {
            return getConnection();
        }
        return c;
    }

    public void release(Connection c) {
        queue.offer(c);
    }

    private boolean check(Connection c) {
        try {
            return c.isValid(1);
        } catch (SQLException e) {
            // e.printStackTrace();
        }
        return false;
    }

    public void shutdown() {
        for (Connection connection : queue) {
            close(connection);
        }
        queue.clear();
    }

    private void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

}
