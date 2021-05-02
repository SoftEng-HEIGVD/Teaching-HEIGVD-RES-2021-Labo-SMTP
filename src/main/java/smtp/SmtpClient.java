package smtp;

import model.exception.SmtpException;
import model.mail.Message;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class SmtpClient {
    public static final String CMD_EHLO = "EHLO ";
    public static final String CMD_MAIL_FROM = "MAIL FROM: ";
    public static final String CMD_RCPT_TO = "RCPT TO: ";
    public static final String CMD_DATA = "DATA";
    public static final String CMD_END_OF_DATA = "\r\n.\r\n";

    private String serverAddress;
    private int port;

    public SmtpClient(String smtpServerAddress, int port) {
        serverAddress = smtpServerAddress;
        this.port = port;
    }

    public void sendMessage(Message message) throws IOException, SmtpException {
        Socket clientSocket = new Socket(serverAddress, port);
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

        String line = reader.readLine();
        writer.print(CMD_EHLO + "localhost\r\n");
        writer.flush();

        line = reader.readLine();
        if(!line.startsWith("250")) {
            throw new SmtpException("Error : " + line);
        }

        while(line.startsWith("250-")) {
            line = reader.readLine();
        }

        writer.write(CMD_MAIL_FROM + message.getFrom() + "\r\n");
        writer.flush();

        line = reader.readLine();
        if(!line.startsWith("250")) {
            throw new SmtpException("Error : " + line);
        }

        for(String t : message.getTo()) {
            writer.write(CMD_RCPT_TO + t + "\r\n");
            writer.flush();

            line = reader.readLine();
            if(!line.startsWith("250")) {
                throw new SmtpException("Error : " + line);
            }
        }

        writer.write(CMD_DATA + "\r\n");
        writer.flush();
        line = reader.readLine();

        writer.write("From: " + message.getFrom() + "\r\n");
        if(message.getCc() != null) {
            writer.write("Cc: " + message.getCc() + "\r\n");
        }
        writer.write("To: " + message.getTo().get(0) + "\r\n");
        for(int i = 1; i < message.getTo().size(); i++) {
            writer.write(", " + message.getTo().get(i) + "\r\n");
        }

        writer.write("Subject: " + message.getSubject() + "\r\n");
        writer.write("\r\n");
        writer.write(message.getBody());
        writer.write(CMD_END_OF_DATA);
        writer.flush();

        line = reader.readLine();
        if(!line.startsWith("250")) {
            throw new SmtpException("Error : " + line);
        }

        writer.write("quit\r\n");
        writer.flush();

        reader.close();
        writer.close();
        clientSocket.close();
    }
}