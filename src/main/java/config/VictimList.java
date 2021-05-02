package config;

import model.exception.EmptyList;
import model.exception.IncorrectFormatEmail;
import model.mail.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class VictimList {
    private ArrayList<Person> people;
    private int sizeList;

    public VictimList(String filename) throws IOException, IncorrectFormatEmail, EmptyList {
        people = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));

        String email;
        while((email = reader.readLine()) != null) {
            people.add(new Person(email));
        }

        if(people.isEmpty()) {
            throw new EmptyList("There is no email in the file");
        }

        sizeList = people.size();
    }

    public ArrayList<Person> getList() {
        return people;
    }

    public int getSize() {
        return sizeList;
    }
}
