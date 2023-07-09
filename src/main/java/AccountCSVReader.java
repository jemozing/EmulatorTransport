import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountCSVReader {
    private static Logger logger;
    public static ArrayList<Account> CSVRead() {
        logger = LoggerFactory.getLogger(AccountCSVReader.class);
        String csvFile = "accounts.csv";
        String csvSplitBy = ",";

        ArrayList<Account> accounts = new ArrayList<>();

        try {
            File file = new File(csvFile);
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(csvSplitBy);
                String login = fields[0];
                String password = fields[1];

                Account account = new Account(login, password);
                accounts.add(account);
            }

            scanner.close();
        } catch (FileNotFoundException e) {

        }

        // Выводим информацию о всех учетных записях
        for (Account account : accounts) {
            logger.debug("Логин: " + account.getLogin() + ", Пароль: " + account.getPassword());
        }
        return accounts;
    }
}
