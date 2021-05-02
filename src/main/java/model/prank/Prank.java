package model.prank;

import model.mail.Message;
import model.mail.Person;

import java.util.ArrayList;

public class Prank {

    private Person sender;
    private ArrayList<Person> recipients;
    private ArrayList<Person> witnesses;

    private Message message;

    private int groupId;

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public ArrayList<Person> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<Person> recipients) {
        this.recipients = recipients;
    }

    public ArrayList<Person> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(ArrayList<Person> witnesses) {
        this.witnesses = witnesses;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
