package main.java.smtp;

import main.java.model.mail.Message;
import main.java.smtp.ISmtpClient;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;



public class SmtpClient implements ISmtpClient {

    private String smtpServerAddress;
    private int smtpServerPort = 2525;

    private PrintWriter writer;
    private BufferedReader reader;

    public SmtpClient(String smtpServerAddress, int port){
        this.smtpServerAddress = smtpServerAddress;
        this.smtpServerPort = port;
    }

    @Override
    public void sendMessage(Message message) throws IOException {

        Socket socket = new Socket(smtpServerAddress, smtpServerPort);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        String line = reader.readLine();
        System.out.println(line);

        writer.print("EHLO localhost\r\n");
        writer.flush();
        line = reader.readLine();
        System.out.println(line);

        if(!line.startsWith("250")){
            throw new IOException("error: " + line);
        }
        while (line.startsWith("250-")){
            line = reader.readLine();
            System.out.println(line);
        }

        writer.write("MAIL FROM: ");
        writer.write(message.getFrom());
        writer.write("\r\n");
        writer.flush();

        line = reader.readLine();
        System.out.println(line);

        for (String to : message.getTo()){
            writer.write("RCPT TO: ");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();

            line = reader.readLine();
            System.out.println(line);
        }

        for (String to : message.getCc()){
            writer.write("RCPT TO: ");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();

            line = reader.readLine();
            System.out.println(line);
        }

        for (String to : message.getBcc()){
            writer.write("RCPT TO: ");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();

            line = reader.readLine();
            System.out.println(line);
        }

        writer.write("DATA");
        writer.write("\r\n");
        writer.flush();

        line = reader.readLine();
        System.out.println(line);

        writer.write("Content-Type: text/plain; charset=\"utf-8\"\r\n");
        writer.write("From: " + message.getFrom() + "\r\n");

        writer.write("To: " + message.getTo()[0]);
        for (int i = 1; i < message.getTo().length; i++){
            writer.write("To: " + message.getTo()[i]);

        }
        writer.write("\r\n");

        writer.write("To: " + message.getCc()[0]);
        for (int i = 1; i < message.getCc().length; i++){
            writer.write("To: " + message.getCc()[i]);

        }
        writer.write("\r\n");

        writer.flush();

        writer.write("Subject: " + message.getSubject());

        writer.write("\r\n");

        writer.flush();

        writer.write(message.getBody());
        writer.write("\r\n");
        writer.write(".");
        writer.write("\r\n");

        writer.flush();

        line = reader.readLine();
        System.out.println(line);

        writer.write("QUIT\r\n");
        writer.flush();
        writer.close();
        reader.close();
        socket.close();
    }
}