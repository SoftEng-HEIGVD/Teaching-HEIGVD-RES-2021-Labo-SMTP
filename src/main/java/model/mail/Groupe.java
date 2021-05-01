package main.java.model.mail;

import java.util.ArrayList;
import java.util.List;

public class Groupe {

    private final List<Personne> members = new ArrayList<>();

    public List<Personne> getMembers() {
        return members;
    }

    public void addMember(Personne person){
        members.add(person);
    }
}