package model.mail;

import model.exception.IncorrectFormatEmail;

public class Person {
    private String email;

    public Person(String email) throws IncorrectFormatEmail {
        if(!correctFormatEmail(email)) {
            throw new IncorrectFormatEmail(email + " does not have a standard email format");
        }

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private boolean correctFormatEmail(String email) {
        return email.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*" +
                "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }
}
