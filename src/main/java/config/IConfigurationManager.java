package main.java.config;

import main.java.model.mail.Personne;

import java.util.List;

public interface IConfigurationManager {
    public List<Personne> getVictims();
    public List<String> getMessages();
    public int getNbGroups();
    public String getSmtpServerAddress();
    public int getSmtpServerPort();
    public List<Personne> getWitnesses();
}
