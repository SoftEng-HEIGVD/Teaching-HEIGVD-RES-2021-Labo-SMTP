package main.java.model.prank;

import main.java.config.IConfigurationManager;
import main.java.model.mail.Groupe;
import main.java.model.mail.Personne;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class PrankGenerator {
    private final IConfigurationManager confManager;

    public PrankGenerator(IConfigurationManager confManager){
        this.confManager = confManager;
    }

    public List<Prank> generatePranks()throws IOException {
        List<Prank> pranks = new ArrayList<>();

        List<String> messages = confManager.getMessages();
        int numMsg = 0;

        int nbGroupes = confManager.getNbGroups();
        int nbVictims = confManager.getVictims().size();
        if (nbVictims / nbGroupes < 3 ){
            throw new IllegalArgumentException("il faut assez de personnes pour en avoir 3 par groupe");
        }
        List<Groupe> groupes = generateGroups(confManager.getVictims(), nbGroupes);
        //on crée une prank par groupe
        for (Groupe g : groupes){
            Prank prank = new Prank();
            List<Personne> victims = g.getMembers();
            //on choisi un envoyeur au hasard
            Collections.shuffle(victims);
            Personne sender = victims.remove(0);
            prank.setSender(sender);
            prank.addVictims(victims);
            prank.addWitnesses(confManager.getWitnesses());

            String message = messages.get(numMsg);
            numMsg = (numMsg + 1) % messages.size();
            prank.setMessage(message);

            pranks.add(prank);
        }
        return pranks;
    }

    public List<Groupe> generateGroups(List<Personne> victims, int nbGroupes){
        List<Groupe> groupes = new ArrayList<Groupe>();
        List<Personne> victimsRemaining = new ArrayList<Personne>(victims);
        //on mélange les victimes pour ne pas avoir à chaque fois les même groupes
        Collections.shuffle(victimsRemaining);
        for (int i = 0; i < nbGroupes; i++){
            Groupe groupe = new Groupe();
            groupes.add(groupe);
        }
        int cpt = 0;
        Groupe currentGroup;
        while (victimsRemaining.size() > 0){
            currentGroup = groupes.get(cpt);
            cpt = (cpt + 1) % groupes.size();
            currentGroup.addMember(victims.remove(0));
        }
        return groupes;
    }
}
