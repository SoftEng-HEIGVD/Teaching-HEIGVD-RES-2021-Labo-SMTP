import config.ConfigurationManager;
import config.ServerProperties;
import model.exception.*;
import model.prank.Prank;
import model.prank.PrankGenerator;
import smtp.SmtpClient;

import java.io.IOException;
import java.util.ArrayList;

public class Spammer {

    public static void main(String... args) throws EmptyList, IOException, IncorrectFormatEmail, GroupMinimumSizeException, SmtpException, NoGroupCreated {
        ConfigurationManager config = new ConfigurationManager();
        PrankGenerator pg = new PrankGenerator(config);

        pg.generateGroups();
        ArrayList<Prank> pranks = pg.generatePranks();

        ServerProperties server = config.getServerProperties();
        String address = server.getServerAddress();
        int port = server.getServerPort();
        int nbGroups = server.getNbVictimGroups();

        SmtpClient smtp = new SmtpClient(address, port);

        if(server.getChosenGroups()[0] == 0) {
            for(Prank p : pranks) {
                smtp.sendMessage(p);
            }
        } else {
            int[] ids = server.getChosenGroups();
            for (int id : ids) {
                int groupId = id - 1;
                if (groupId < nbGroups) {
                    smtp.sendMessage(pranks.get(groupId));
                } else {
                    throw new IndexOutOfBoundsException("GroupId exceeds the number of groups");
                }
            }
        }
    }
}
