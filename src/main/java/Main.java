package main.java;

import main.java.config.ConfigurationManager;
import main.java.model.prank.Prank;
import main.java.model.prank.PrankGenerator;
import main.java.smtp.SmtpClient;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException {
        ConfigurationManager cm = new ConfigurationManager();
        PrankGenerator pg = new PrankGenerator(cm);
        SmtpClient sc = new SmtpClient(cm.getSmtpServerAddress(), cm.getSmtpServerPort());
        List<Prank> pranks;
        try{
            pranks = pg.generatePranks();
            for (Prank p : pranks){
                sc.sendMessage(p.generateMailMessage());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
