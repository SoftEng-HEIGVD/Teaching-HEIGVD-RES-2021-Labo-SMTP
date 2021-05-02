package config;

import model.exception.EmptyList;
import model.exception.IncorrectFormatEmail;

import java.io.IOException;

public class ConfigurationManager {
    private final ServerProperties serverProperties;
    private final MessageList messageList;
    private final VictimList victimList;

    public ConfigurationManager() throws EmptyList, IOException, IncorrectFormatEmail {
        serverProperties = new ServerProperties("data/config.properties");
        serverProperties.loadFromFile();

        messageList = new MessageList("data/messages.utf8");
        victimList = new VictimList("data/victims.utf8");
    }

    public ServerProperties getServerProperties() {
        return serverProperties;
    }

    public MessageList getMessageList() {
        return messageList;
    }

    public VictimList getVictimList() {
        return victimList;
    }
}
