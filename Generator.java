import java.util.Scanner;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.io.Console;

public class Generator {

    public static ArrayList<String> tempUserLog = new ArrayList<String>();
    public static ArrayList<String> tempPassLog = new ArrayList<String>();

    static String file = "Database_data.txt";
    static boolean alreadyExecuted = false;

    public static void main(String[] args) {

        saveToDatabase();
        clearScreen();
        startupMenu(args);

    }

    // Saves Data from Database to textfile
    public static void saveToDatabase() {

        try {

            File dataFile = new File(file);

            if (!dataFile.exists()) {

                dataFile.createNewFile();

            } else {

                String path = dataFile.getAbsolutePath();

                FileWriter fw = new FileWriter(path, true);
                for (int i = 0; i < tempUserLog.size(); i++) {
                    fw.write(tempUserLog.get(i) + "\n" + tempPassLog.get(i) + "\n");
                }

                fw.close();

            }

        } catch (IOException e) {
            System.out.println(" An error occurred!");
            e.printStackTrace();
        }

    }

    public static void startupMenu(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("\n Welcome!");

        System.out.println("\n 1) Generate random Username/Password");
        System.out.println(" 2) View Database log \033[32m(WIP)\033[0m");

        int input1;

        System.out.print("\n Choose (Ctrl + C to exit): ");
        input1 = scan.nextInt();

        if (input1 == 1) {
            generateUsername_Password(args);
        } else if (input1 == 2) {
            printDatabase(args);
        } else {

            do {

                clearScreen();
                System.out.println("\n (\033[31mError\033[0m): Invalid input. Please retry!");
                startupMenu(args);

            } while (!true);

        }

        scan.close();

    }

    public static void generateUsername_Password(String[] args) {

        WordList u = new WordList();
        SecureRandom sr = new SecureRandom();
        Scanner s = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");

        int psdAmtChar;

        do {

            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");
            }

            psdAmtChar = scanner.nextInt();

            if (!(psdAmtChar >= 8 && psdAmtChar <= 50)) {
                System.out.printf("\n (\033[31mError\033[0m): Not in range! \"%s\"\n", psdAmtChar);
                System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");
            }

        } while (!(psdAmtChar >= 8 && psdAmtChar <= 50));

        do {

            clearScreen();
            String choice = "", temp, username = "";

            char[] password = new char[psdAmtChar];

            for (int usrElementPlace = 0; usrElementPlace < u.names.length; usrElementPlace++) {
                // int randomA = r.nextInt((u.names.length - 1) - 0 + 1);
                int randomA = sr.nextInt(u.names.length);
                username = u.names[randomA];
            }

            for (int psdElementPlace = 0; psdElementPlace < password.length; psdElementPlace++) {
                // int randomB = r.nextInt((u.characters.length - 1) - 0 + 1);
                int randomB = sr.nextInt(u.characters.length);
                password[psdElementPlace] = u.characters[randomB];
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            System.out.println("\n We've successfully generated you username and password!\n");
            String strPassword = String.valueOf(password);

            temp = String.format("%-50s | %-12s | %-19s", strPassword, sr.getAlgorithm(), dtf.format(now));

            System.out.println("  Generated Username/Password\n  ───────────────────────────\n");
            System.out.println("  +───────────+────────────────────────────────────────────────────+");
            System.out.printf("  | Username: | %-50s |\n", username);
            System.out.println("  +───────────+────────────────────────────────────────────────────+");
            System.out.printf("  | Password: | %-50s |\n", strPassword);
            System.out.println("  +───────────+────────────────────────────────────────────────────+\n");

            do {

                System.out.print(" Do you want to claim this password (Y/N)? ");
                choice = s.nextLine();

            } while (!(choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N")));

            if (choice.equalsIgnoreCase("Y")) {

                tempUserLog.add(username);
                tempPassLog.add(temp);
                saveToDatabase();
                tempUserLog.clear();
                tempPassLog.clear();
                break;

            } else {
                continue;
            }

        } while (true);

        main(args);
        System.out.println("\n");

        s.close();
        scanner.close();

    }

    public static void printDatabase(String[] args) {

        File database = new File(file);
        Scanner scanner = new Scanner(System.in);
        Console c = System.console();

        BufferedReader reader;
        String password = "test123";

        if (alreadyExecuted == false) {

            for (int psdTrials = 0, trialsLeft = 3; psdTrials < 3;) {

                System.out.printf("\n Password (Trials left: \033[31m%d\033[0m): ", trialsLeft);
                char[] adminPassword = c.readPassword();

                String convertedPass = String.valueOf(adminPassword);

                if (!convertedPass.equalsIgnoreCase(password)) {
                    System.out.println("\n Incorrect Password!");
                    psdTrials++;
                    trialsLeft--;
                } else if (convertedPass.equalsIgnoreCase(password)) {
                    alreadyExecuted = true;
                    break;
                }

                if (psdTrials >= 3) {
                    clearScreen();
                    System.out.println("\n Password trials exceeded (3 max)!");
                    startupMenu(args);
                }

            }

        }

        clearScreen();

        System.out.println("\n  Database Options\n  ────────────────\n");

        System.out.println("  1) Clear logs");
        System.out.println("  2) Go back to menu\n");

        System.out.println(
                "  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");
        System.out.println(
                "  | No.# │  Claimed Usernames  |                 Claimed Passwords                  |  Algorithm   |  Date/Time Claimed  |");
        System.out.println(
                "  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");

        try {

            File a = new File(file);
            reader = new BufferedReader(new FileReader(file));

            if (database.length() == 0) {
                String empty = "empty";
                System.out.print("  | 0000 |");
                System.out.printf(" %-19s |", empty);
                System.out.printf(" %-50s |", empty);
                System.out.printf(" %-12s |", empty);
                System.out.printf(" %-19s |", empty);
                System.out.println(
                        "\n  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");

                // System.out.printf("\n Database Location ==> %s\n", a.getAbsolutePath());

                databasePrompt(scanner, args);
                main(args);

            } else {

                String line;
                int n = 0;
                try {

                    while ((line = reader.readLine()) != null) {

                        if (n % 2 == 0) {
                            tempUserLog.add(line);
                        } else {
                            tempPassLog.add(line);
                        }
                        n++;
                    }

                    reader.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                // Printing Username/Pass
                for (int counter = 0; counter < tempUserLog.size(); counter++) {

                    DecimalFormat df = new DecimalFormat("0000");
                    String count = df.format(counter);

                    System.out.print("  | " + count + " |");
                    System.out.printf(" %-19s |", tempUserLog.get(counter));
                    System.out.printf(" %-50s |", tempPassLog.get(counter));
                    System.out.println(
                            "\n  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");
                }

                // System.out.printf("\n Database Location ==> %s\n", a.getAbsolutePath());

                tempUserLog.clear();
                tempPassLog.clear();
                databasePrompt(scanner, args);

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        scanner.close();
    }

    public static void databasePrompt(Scanner scanner, String[] args) {

        System.out.print(" \n Choose: ");

        int input1;

        do {

            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print(" \n Choose: ");
            }

            input1 = scanner.nextInt();

            if (!(input1 == 1 || input1 == 2)) {
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input1);
                System.out.print(" \n Choose: ");
            }

        } while (!(input1 == 1 || input1 == 2));

        if (input1 == 1) {
            try {
                FileWriter clearLog = new FileWriter(file);
                clearLog.write("");
                clearLog.close();
                printDatabase(args);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (input1 == 2) {
            main(args);
        }

    }

    public static String encrypt(String A, String B) {
        // Encryption goes later
        return B;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
