package model.mail;

import java.util.ArrayList;

public class Group {
    private static int nbGroups = 0;

    private int id;
    private ArrayList<Person> members;

    public Group() {
        id = ++nbGroups;
        members = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Person> getMembers() {
        return members;
    }

    public Person getMember(int index) {
        return members.get(index);
    }

    public void addMember(Person person) {
        members.add(person);
    }

    public int size() {
        return members.size();
    }

    @Override
    public String toString() {
        String res = "Group " + id + "\r\n";
        res += "Members :" + "\r\n";
        for(Person p : members) {
            res += p + "\r\n";
        }

        return res;
    }
}
