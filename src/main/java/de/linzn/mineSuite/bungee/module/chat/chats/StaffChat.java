package de.linzn.mineSuite.bungee.module.chat.chats;

import de.linzn.mineSuite.bungee.module.chat.IChatChannel;
import de.linzn.mineSuite.bungee.module.chat.socket.JServerChatOutput;
import de.linzn.mineSuite.bungee.utils.ChatFormate;

public class StaffChat implements IChatChannel {

    @Override
    public void sendChat(String sender, String text, String prefix, String suffix) {
        String formattedText = ChatFormate.genStaffChat(sender, text, prefix);
        JServerChatOutput.staffChat(formattedText);
    }

    @Override
    public String getChannelName() {
        return "STAFF";
    }
}
