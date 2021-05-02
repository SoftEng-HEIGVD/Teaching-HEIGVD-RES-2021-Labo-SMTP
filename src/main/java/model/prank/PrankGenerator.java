package model.prank;

import config.ConfigurationManager;
import config.MessageList;
import config.ServerProperties;
import config.VictimList;
import model.exception.GroupMinimumSizeException;
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
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

public class PrankGenerator {
    private final static int MINIMUM_GROUP_SIZE = 3;
    private final static String END_OF_GROUP = "==========";

    private ServerProperties server;
    private MessageList messages;
    private VictimList victims;

    private Group[] groups;

    public PrankGenerator(ConfigurationManager config) {
        server = config.getServerProperties();
        messages = config.getMessageList();
        victims = config.getVictimList();

        groups = null;
    }

    public ArrayList<Prank> generatePranks() throws NoGroupCreated, IOException {
        if(groups == null) {
            throw new NoGroupCreated("Please form groups before generate pranks");
        }

        ArrayList<Prank> pranks = new ArrayList<>();
        for(int i = 0; i < groups.length; i++) {
            Prank p = new Prank();

            int randomIndex = Utils.randInt(groups[i].size());
            Person sender = groups[i].getMember(randomIndex);

            ArrayList<Person> recipients = groups[i].getMembers();
            recipients.remove(randomIndex);

            p.setSender(sender);
            p.setRecipients(recipients);

            Message message = messages.getRandomMessage();

            message.setFrom(sender.getEmail());
            message.setTo(personsToEmails(recipients));
            if(!server.getEmailCCs()[0].equalsIgnoreCase("none")) {
                message.setCc(new ArrayList<>(Arrays.asList(server.getEmailCCs())));
            }
            p.setMessage(message);

            p.setGroupId(groups[i].getId());

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
            throw new GroupMinimumSizeException("Every group must be at least " + MINIMUM_GROUP_SIZE);
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

    private ArrayList<String> personsToEmails(ArrayList<Person> persons) {
        ArrayList<String> res = new ArrayList<>();
        for(Person p : persons) {
            res.add(p.getEmail());
        }

        return res;
    }

    private void createGroupFile(ArrayList<Prank> pranks) throws IOException {
        File outDir = new File("out");
        if(!outDir.exists()) {
            Files.createDirectory(Paths.get("out"));
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
            content.append("Group " + p.getGroupId() + "\r\n");
            content.append("From:" + p.getSender().getEmail() + "\r\n");
            content.append("To: " + "\r\n");
            for(Person r : p.getRecipients()) {
                content.append("\t" + r.getEmail() + "\r\n");
            }
            content.append("\r\n");
            content.append(END_OF_GROUP + "\r\n");
        }

        writer.println(content);
        writer.flush();
        writer.close();
    }
}
