package main.java.model.prank;

import main.java.model.mail.Message;
import main.java.model.mail.Personne;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Prank {

    private Personne sender;
    private final List<Personne> victims = new ArrayList<>();
    private final List<Personne> witnesses = new ArrayList<>();
    private String message;

    public Personne getSender() {
        return sender;
    }

    public List<Personne> getVictims() {
        return victims;
    }

    public List<Personne> getWitnesses() {
        return witnesses;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(Personne sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addVictims(List<Personne> victims){
        this.victims.addAll(victims);
    }

    public void addWitnesses(List<Personne> witnesses){
        this.witnesses.addAll(witnesses);
    }

    public Message generateMailMessage(){
        Message msg = new Message();
        //écriture du corps du message
        msg.setBody(message + "\r\n" + sender.getFirstName());
        //on destine le message aux victimes
        msg.setTo(victims.stream().map(Personne::getAddress).collect(Collectors.toList()).toArray(new String[]{}));
        //les témoins sont en cc
        msg.setCc(witnesses.stream().map(Personne::getAddress).collect(Collectors.toList()).toArray(new String[]{}));
        //on assigne l'envoyeur
        msg.setFrom(sender.getAddress());
        return msg;
    }
}
