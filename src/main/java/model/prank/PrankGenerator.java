package model.prank;

import config.ConfigurationManager;
import config.MessageList;
import config.ServerProperties;
import config.VictimList;
import model.exception.GroupMinimumSizeException;
import model.exception.IncorrectFormatEmail;
import model.exception.NoGroupCreated;
import model.mail.Group;
import model.mail.Message;
import model.mail.Person;
import utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import static model.mail.Message.CRLF;

public class PrankGenerator {
    private final static int MINIMUM_GROUP_SIZE = 3;
    private final static String END_OF_GROUP = "==========";

    private final ServerProperties server;
    private final MessageList messages;
    private final VictimList victims;

    private Group[] groups;

    public PrankGenerator(ConfigurationManager config) {
        server = config.getServerProperties();
        messages = config.getMessageList();
        victims = config.getVictimList();

        groups = null;
    }

    public ArrayList<Prank> generatePranks() throws NoGroupCreated, IOException, IncorrectFormatEmail {
        if(groups == null) {
            throw new NoGroupCreated("Please form groups before generate pranks");
        }

        ArrayList<Prank> pranks = new ArrayList<>();
        for (Group group : groups) {
            Prank p = new Prank();

            int randomIndex = Utils.randInt(group.size());
            Person sender = group.getMember(randomIndex);

            ArrayList<Person> recipients = group.getMembers();
            recipients.remove(randomIndex);

            p.setSender(sender);
            p.setRecipients(recipients);

            if (!server.getEmailCCs()[0].equalsIgnoreCase("none")) {
                ArrayList<Person> ccs = new ArrayList<>();
                for (String email : server.getEmailCCs()) {
                    ccs.add(new Person(email));
                }
                p.setWitnesses(ccs);
            }

            Message message = messages.getRandomMessage();
            p.setMessage(message);

            p.setGroupId(group.getId());

            pranks.add(p);
        }

        createGroupFile(pranks);

        return pranks;
    }

    public void generateGroups() throws GroupMinimumSizeException {
        int nbVictims = victims.getSize();
        int nbGroups = server.getNbVictimGroups();

        groups = new Group[nbGroups];

        if(nbVictims % nbGroups < MINIMUM_GROUP_SIZE || nbVictims < nbGroups * MINIMUM_GROUP_SIZE) {
            throw new GroupMinimumSizeException("There is a group with size less than " + MINIMUM_GROUP_SIZE);
        }

        int groupSize = (int) Math.ceil((double)nbVictims / nbGroups);

        ArrayList<Person> vL = victims.getList();

        for(int i = 0, j = -1; i < nbVictims; i++) {
            if(i % groupSize == 0) {
                j++;
                groups[j] = new Group();
            }
            groups[j].addMember(vL.get(i));
        }
    }

    private void createGroupFile(ArrayList<Prank> pranks) throws IOException {
        File outDir = new File("target\\out");
        if(!outDir.exists()) {
            Files.createDirectory(Paths.get("target\\out"));
        } else {
            File[] filesOut = outDir.listFiles();
            if(filesOut != null) {
                for(File f : filesOut) {
                    if(f.isDirectory()) {
                        FileUtils.deleteDirectory(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }

        PrintWriter writer = new PrintWriter(
                new FileWriter(outDir.getPath() + "\\groups.utf8", StandardCharsets.UTF_8));

        StringBuilder content = new StringBuilder();

        for(Prank p : pranks) {
            content.append("Group ").append(p.getGroupId()).append(" - ").append(p.numberOfMembers()).append(" members").append(CRLF);
            content.append("From: ").append(p.getSender().getEmail()).append(CRLF);
            content.append("To:" + CRLF);
            for(Person r : p.getRecipients()) {
                content.append("\t").append(r.getEmail()).append(CRLF);
            }
            content.append(CRLF);
            content.append(END_OF_GROUP + CRLF);
        }

        writer.println(content);
        writer.flush();
        writer.close();
    }
}
