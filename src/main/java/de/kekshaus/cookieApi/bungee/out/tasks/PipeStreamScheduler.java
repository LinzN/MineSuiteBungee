package de.kekshaus.cookieApi.bungee.out.tasks;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.kekshaus.cookieApi.bungee.CookieApiBungee;
import net.md_5.bungee.api.ProxyServer;

public class PipeStreamScheduler {

	public PipeStreamScheduler() {
		ProxyServer.getInstance().getScheduler().schedule(CookieApiBungee.instance, new Runnable() {

			public void run() {
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bytes);

				try {
					out.writeUTF("Connected (Bungee)");
				} catch (IOException e) {
					e.printStackTrace();
				}

				ProxyServer.getInstance().getScheduler().runAsync(CookieApiBungee.instance, new PipeStream(bytes));
			}
		}, 3L, 60L, TimeUnit.SECONDS);
	}

}