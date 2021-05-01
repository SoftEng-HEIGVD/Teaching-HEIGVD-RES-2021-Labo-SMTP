package main.java.smtp;

import main.java.model.mail.Message;

import java.io.IOException;

public interface ISmtpClient {
    public void sendMessage(Message message) throws IOException;
}
