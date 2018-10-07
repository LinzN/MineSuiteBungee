package de.linzn.mineSuite.bungee.module.chat;

public interface IChatChannel {

    void sendChat(String sender, String text, String prefix, String suffix);

    String getChannelName();

}
