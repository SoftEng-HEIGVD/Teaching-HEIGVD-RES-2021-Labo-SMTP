package smtp;

import model.exception.SmtpException;
import model.mail.Message;
import model.mail.Person;
import model.prank.Prank;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

import static model.mail.Message.CRLF;

public final class SmtpClient {
    public static final String CMD_EHLO = "EHLO ";
    public static final String CMD_MAIL_FROM = "MAIL FROM: ";
    public static final String CMD_RCPT_TO = "RCPT TO: ";
    public static final String CMD_DATA = "DATA";
    public static final String CMD_END_OF_DATA = CRLF + "." + CRLF;

    private String serverAddress;
    private int port;

    public SmtpClient(String smtpServerAddress, int port) {
        serverAddress = smtpServerAddress;
        this.port = port;
    }

    public void sendMessage(Prank prank) throws IOException, SmtpException {
        Socket clientSocket = new Socket(serverAddress, port);
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

        Message message = prank.getMessage();
        String sender = prank.getSender().getEmail();

        ArrayList<String> recipients = new ArrayList<>();
        for(Person p : prank.getRecipients()) {
            recipients.add(p.getEmail());
        }

        String line = reader.readLine();
        writer.print(CMD_EHLO + "localhost" + CRLF);
        writer.flush();

        line = reader.readLine();
        if(!line.startsWith("250")) {
            throw new SmtpException("Error : " + line);
        }

        while(line.startsWith("250-")) {
            line = reader.readLine();
        }

        writer.write(CMD_MAIL_FROM + sender + CRLF);
        writer.flush();

        line = reader.readLine();
        if(!line.startsWith("250")) {
            throw new SmtpException("Error : " + line);
        }

        for(String r : recipients) {
            writer.write(CMD_RCPT_TO + r + CRLF);
            writer.flush();

            line = reader.readLine();
            if(!line.startsWith("250")) {
                throw new SmtpException("Error : " + line);
            }
        }

        writer.write(CMD_DATA + CRLF);
        writer.flush();
        line = reader.readLine();

        writer.write("From: " + sender + CRLF);


        if(prank.getWitnesses() != null) {
            ArrayList<String> ccs = new ArrayList<>();
            for(Person p : prank.getWitnesses()) {
                ccs.add(p.getEmail());
            }

            writer.write("Cc: " + ccs.get(0) + CRLF);
            for(int i = 1; i < ccs.size(); i++) {
                writer.write(", " + ccs.get(i) + CRLF);
            }
        }
        writer.write("To: " + recipients.get(0) + CRLF);
        for(int i = 1; i < recipients.size(); i++) {
            writer.write(", " + recipients.get(i) + CRLF);
        }

        writer.write("Subject: " + "=?utf-8?B?" + Base64.getEncoder().encodeToString(message.getSubject().getBytes()) + "?="  + CRLF);
        writer.write(Message.contentType + CRLF);
        writer.write(CRLF);
        writer.write(message.getBody());
        writer.write(CMD_END_OF_DATA);
        writer.flush();

        line = reader.readLine();
        if(!line.startsWith("250")) {
            throw new SmtpException("Error : " + line);
        }

        writer.write("quit" + CRLF);
        writer.flush();

        reader.close();
        writer.close();
        clientSocket.close();
    }
}