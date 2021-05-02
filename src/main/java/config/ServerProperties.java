package config;

import java.io.*;
import java.util.Properties;

public class ServerProperties {
    private final static int DEFAULT_PORT = 25;

    private final String filename;

    private String serverAddress;
    private int serverPort;
    private int nbVictimGroups;
    private String[] emailCCs;

    private int[] chosenGroups;

    public ServerProperties(String filename) {
        this.filename = filename;
    }

    public void loadFromFile() throws IOException {
        FileInputStream input = new FileInputStream(filename);
        Properties serverConfig = new Properties();
        serverConfig.load(input);

        serverAddress = serverConfig.getProperty("smtpServerAddress");

        serverPort = Integer.parseInt(serverConfig.getProperty("smtpServerPort"));
        if(serverPort <= 0) {
            serverPort = DEFAULT_PORT;
        }

        nbVictimGroups = Integer.parseInt(serverConfig.getProperty("numberOfGroups"));
        emailCCs = serverConfig.getProperty("witnessesToCC").split(",");

        String[] groupsInString = serverConfig.getProperty("chosenGroupsForPrank").split(",");

        chosenGroups = new int[groupsInString.length];
        for(int i = 0; i < chosenGroups.length; i++) {
            chosenGroups[i] = Integer.parseInt(groupsInString[i]);
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getNbVictimGroups() {
        return nbVictimGroups;
    }

    public String[] getEmailCCs() {
        return emailCCs;
    }

    public int[] getChosenGroups() {
        return chosenGroups;
    }

}
