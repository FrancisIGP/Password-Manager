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

public class Generator {

    public static ArrayList<String> tempUserLog = new ArrayList<String>();
    public static ArrayList<String> tempPassLog = new ArrayList<String>();

    static String file = "Database_data.txt";

    public static void main(String[] args) {

        saveData();
        clearScreen();
        startupMenu(args);

    }

    // Saves Data from Database to textfile
    public static void saveData() {

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
            System.out.println(" An error occurred.");
            e.printStackTrace();
        }

    }

    public static void startupMenu(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("\n Welcome!");

        System.out.println("\n 1) Generate random Username/Password");
        System.out.println(" 2) Print Database log");

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
                System.out.println("\n Error: Invalid input. Please retry!");
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

        int psdAmtChar = 0;

        try {

            System.out.print("\n How many characters do you want for your password (50 max)? ");
            psdAmtChar = s.nextInt();

            s.nextLine();

            if (psdAmtChar < 1 || psdAmtChar > 50) {

                System.out.println("\n Should at least be greater than '1' or less than '50'");
                System.out.print("\n Press \033[32m[ENTER]\033[0m to retry (Ctrl + C to exit)...");
                scanner.nextLine();
                main(args);

            }

        } catch (Exception e) {

            System.out.println("\n Invalid input. Please retry!");
            System.out.print("\n Press \033[32m[ENTER]\033[0m to retry (Ctrl + C to exit)...");
            scanner.nextLine();
            main(args);

        }

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
                saveData();
                tempUserLog.clear();
                tempPassLog.clear();
                break;

            } else {
                continue;
            }

        } while (true);

        // System.out.print("\n\n Press \033[32m[ENTER]\033[0m to repeat (Ctrl + C to
        // exit)...");
        // scanner.nextLine();

        main(args);
        System.out.println("\n");

        s.close();
        scanner.close();

    }

    public static void printDatabase(String[] args) {

        // Boarder Header
        System.out.println("\n  Database log\n  ────────────\n");
        System.out.println("  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");
        System.out.println("  | No.# │    Usernames log    |                   Passwords log                    |   Algorithm  |   Date/Time Logged  |");
        System.out.println("  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");

        File database = new File(file);
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader;
        try {

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

                System.out.print("\n Press \033[32m[ENTER]\033[0m to repeat (Ctrl + C to exit)...");
                scanner.nextLine();

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

                System.out.print(" \n Press \033[32m[ENTER]\033[0m to repeat (Ctrl + C to exit)...");
                scanner.nextLine();

                tempUserLog.clear();
                tempPassLog.clear();
                main(args);

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        scanner.close();
    }

    public static String encrypt(String A, String B) {

        return B;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
