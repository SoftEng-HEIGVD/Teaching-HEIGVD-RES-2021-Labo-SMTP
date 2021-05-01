package main.java.config;

import main.java.model.mail.Personne;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfigurationManager implements IConfigurationManager{
    final List<Personne> victims;
    final List<String> messages;
    int nbGroups;
    String smtpServerAddress;
    int smtpServerPort;
    List<Personne> witnesses;

    public ConfigurationManager() throws IOException{
        //on récupère le contenu des fichiers de config
        victims = getFileAddresses("./config/victims.utf8");
        messages = getFileMessages("./config/messages.utf8");
        loadProperties("./config/config.properties");
    }

    private void loadProperties(String filePath) throws IOException{
        FileInputStream fis = new FileInputStream(filePath);
        Properties properties = new Properties();
        properties.load(fis);
        //on récupère les proprietés précisées dans config.properties
        smtpServerAddress = properties.getProperty("smtpServerAddress");
        //le retour de la fonction getProperty étant en String, il est nécessaire de le convertir en int
        smtpServerPort = Integer.parseInt(properties.getProperty("smtpServerPort"));
        nbGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));

        witnesses = new ArrayList<Personne>();
        String tmp = properties.getProperty("witnessesToCC");
        String[] addresses = tmp.split(",");
        for (String a : addresses){
            witnesses.add(new Personne(a));
        }
    }

    private List<Personne> getFileAddresses(String filePath)throws IOException{
        List<Personne> adresses;
        try (FileInputStream fis = new FileInputStream(filePath)){
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            try (BufferedReader br = new BufferedReader(isr)){
                adresses = new ArrayList<>();
                String adress = br.readLine();
                while (adress != null){
                    adresses.add(new Personne(adress));
                    adress = br.readLine();;
                }
            }
        }
        return adresses;
    }

    private List<String> getFileMessages(String filePath)throws IOException{
        List<String> messages;
        try (FileInputStream fis = new FileInputStream(filePath)){
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            try (BufferedReader br = new BufferedReader(isr)){
                messages = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                String message = br.readLine();
                while (message != null){
                     sb.append(message);
                     sb.append("\n");
                     message = br.readLine();;
                }
                messages = Arrays.asList(sb.toString().split("==\n"));
            }
        }
        return messages;
    }


    public List<Personne> getVictims(){
        return victims;
    }
    public List<String> getMessages(){
        return messages;
    }
    public int getNbGroups(){
        return nbGroups;
    }
    public String getSmtpServerAddress(){
        return smtpServerAddress;
    }
    public int getSmtpServerPort(){
        return smtpServerPort;
    }
    public List<Personne> getWitnesses(){
        return witnesses;
    }

}
