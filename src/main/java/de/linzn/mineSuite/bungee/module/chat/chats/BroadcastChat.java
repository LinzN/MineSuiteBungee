package de.linzn.mineSuite.bungee.module.chat.chats;

import de.linzn.mineSuite.bungee.module.chat.IChatChannel;
import de.linzn.mineSuite.bungee.utils.ChatFormate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BroadcastChat implements IChatChannel {

    @Override
    public void sendChat(String sender, String text, String prefix, String suffix) {
        String formattedText = ChatFormate.genBroadcastChat(text);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            TextComponent vote = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
            p.sendMessage(vote);
        }
        ProxyServer.getInstance().getLogger().info(text);
    }

    @Override
    public String getChannelName() {
        return "BROADCAST";
    }
}
