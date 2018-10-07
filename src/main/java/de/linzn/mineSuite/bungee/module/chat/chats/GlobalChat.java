package de.linzn.mineSuite.bungee.module.chat.chats;

import de.linzn.mineSuite.bungee.module.chat.IChatChannel;
import de.linzn.mineSuite.bungee.module.core.BungeeManager;
import de.linzn.mineSuite.bungee.utils.ChatFormate;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GlobalChat implements IChatChannel {

    @Override
    public void sendChat(String sender, String text, String prefix, String suffix) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);

        if (player == null) {
            return;
        }
        String formattedText = ChatFormate.genGlobalChat(sender, text, prefix, suffix);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            BungeeManager.sendMessageToTarget(p, formattedText);
        }
        ProxyServer.getInstance().getLogger().info(formattedText);
    }

    @Override
    public String getChannelName() {
        return "GLOBAL";
    }
}
