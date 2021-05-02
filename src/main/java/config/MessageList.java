package config;

import model.exception.EmptyList;
import model.mail.Message;
import utils.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MessageList {
    private final static String END_OF_MESSAGE = "(==========)";

    private Message[] messages;
    private int nbMessages;

    public MessageList(String filename) throws IOException, EmptyList {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));

        addEachMessageToList(reader);
    }

    public Message[] getMessages() {
        return messages;
    }

    public int getNbMessages() {
        return nbMessages;
    }

    public Message getRandomMessage() {
        int randomIndex = Utils.randInt(messages.length);
        return messages[randomIndex];
    }

    private void addEachMessageToList(BufferedReader reader) throws IOException, EmptyList {
        StringBuilder allMessages = new StringBuilder();
        String message;
        while((message = reader.readLine()) != null) {
            allMessages.append(message).append("\r\n");
        }

        if(allMessages.length() == 0) {
            throw new EmptyList("There is no message in the file");
        }

        String[] splitedMessages = allMessages.toString().split(END_OF_MESSAGE);
        nbMessages = splitedMessages.length;

        StringBuilder[] splitedMessagesBuilder = new StringBuilder[nbMessages];
        splitedMessagesBuilder[0] = new StringBuilder(splitedMessages[0]);
        for(int i = 1; i < nbMessages; i++) {
            splitedMessagesBuilder[i] = new StringBuilder(splitedMessages[i]);

            //delete '\r' at the beginning of each message
            splitedMessagesBuilder[i].deleteCharAt(0);
            //delete '\n' at the beginning of each message
            splitedMessagesBuilder[i].deleteCharAt(0);
        }

        messages = new Message[nbMessages];

        for(int i = 0; i < nbMessages; i++) {
            messages[i] = new Message();
            String[] content = splitedMessagesBuilder[i].toString().split("\r\n", 2);

            String subject = (content[0].split(".*: "))[1];
            String body = content[1];

            messages[i].setSubject(subject);
            messages[i].setBody(body);
        }
    }
}
