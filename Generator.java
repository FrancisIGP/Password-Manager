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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Generator {

    public static ArrayList<String> tempUserLog = new ArrayList<String>();
    public static ArrayList<String> tempPassLog = new ArrayList<String>();
    public static ArrayList<String> extraArray = new ArrayList<String>();

    static String file = "Your_Personal_Vault.txt";
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
                    String tempVar = encrypt(tempUserLog.get(i), pVal) + "\n" + encrypt(tempPassLog.get(i), pVal)
                            + "\n";
                    fw.write(tempVar);
                }

                fw.close();

            }

        } catch (IOException e) {
            System.out.println(" An error occurred!");
            e.printStackTrace();
        }

    }

    static String pVal = WordList.tempTesting;

    public static void startupMenu(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("\n Main menu \033[32m(Welcome!)\033[0m\n ────────────────────\n");

        System.out.println(" [1] Generate random Username/Password");
        System.out.println(" [2] View/Manage your vault \033[32m(Password Protected)\033[0m");
        System.out.println(" [3] Program info \033[31m[?]\033[0m");

        int input1;

        System.out.print("\n Choose (Ctrl + C to exit): ");
        input1 = scan.nextInt();

        if (input1 == 1) {
            generateUsername_Password(args);
        } else if (input1 == 2) {
            printDatabase(args);
        } else if (input1 == 3) {

            System.out.println("\n \033[31m[?]\033[0m Program info");
            System.out.println("\n Program: Password Manager");
            System.out.println(" Version: BETA");
            System.out.println(" Github: https://github.com/FrancisIGP");
            System.out.println(" Developer: FrancisIGP");

            scan.nextLine();
            System.out.print("\n Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
            scan.nextLine();

            main(args);

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
        Scanner scan = new Scanner(file);

        System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");

        int psdAmtChar;

        do {

            while (!s.hasNextInt()) {
                String input = s.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");
            }

            psdAmtChar = s.nextInt();

            if (!(psdAmtChar >= 8 && psdAmtChar <= 50)) {
                System.out.printf("\n (\033[31mError\033[0m): Not in range! \"%s\"\n", psdAmtChar);
                System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");
            }

        } while (!(psdAmtChar >= 8 && psdAmtChar <= 50));

        s.nextLine();

        do {

            clearScreen();

            String choice = "", username = "", temp;
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
            System.out.println("  +───────────+────────────────────────────────────────────────────+");

            do {

                System.out.print("\n Do you want to claim and save this username/password (Y[yes]/N[no]/Q[quit])? ");
                choice = s.nextLine();

            } while (!(choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("Q")));

            if (choice.equalsIgnoreCase("Y")) {

                saveUserPassToDatabase(username, temp);
                System.out.println(
                        "\n \033[32m[i]\033[0m Your username and password are successfuly stored into your vault!\n");
                break;

            } else if (choice.equalsIgnoreCase("N")) {
                continue;
            } else {
                main(args);
                break;
            }

        } while (true);

        System.out.print(" Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
        s.nextLine();

        main(args);
        System.out.println("\n");

        s.close();
        scan.close();

    }

    public static void printDatabase(String[] args) {

        File database = new File(file);
        Scanner scanner = new Scanner(System.in);
        Console c = System.console();

        BufferedReader reader;
        String cp = "";

        if (alreadyExecuted == false) {

            for (int psdTrials = 0, trialsLeft = 3; psdTrials < 3;) {

                System.out.printf("\n Password (Trials left: \033[31m%d\033[0m): ", trialsLeft);
                char[] ap = c.readPassword();

                cp = String.valueOf(ap);

                if (!cp.equalsIgnoreCase(pVal)) {
                    System.out.println("\n Incorrect Password!");
                    psdTrials++;
                    trialsLeft--;
                } else {
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

        System.out.println("  [1] Clear vault");
        System.out.println("  [2] Remove username/password \033[32m(WIP)\033[0m");
        System.out.println("  [3] Add new Username/Password");
        System.out.println("  [4] Go back to menu\n");

        System.out.println(
                "  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");
        System.out.println(
                "  | No.# │   Your Usernames    |                   Your Passwords                   |  Algorithm   |  Date/Time Created  |");
        System.out.println(
                "  +──────+─────────────────────+────────────────────────────────────────────────────+──────────────+─────────────────────+");

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

                // System.out.printf("\n Database Location ==> %s\n", a.getAbsolutePath());

                databasePrompt(scanner, args);
                main(args);

            } else {

                String line;
                int n = 0;
                try {

                    while ((line = reader.readLine()) != null) {

                        if (n % 2 == 0) {
                            tempUserLog.add(decrypt(line, pVal));
                        } else {
                            tempPassLog.add(decrypt(line, pVal));
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

        System.out.print(" \n  Choose: ");

        int input1;

        do {

            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print(" \n Choose: ");
            }

            input1 = scanner.nextInt();

            if (!(input1 == 1 || input1 == 2 || input1 == 3 || input1 == 4)) {
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input1);
                System.out.print(" \n Choose: ");
            }

        } while (!(input1 == 1 || input1 == 2 || input1 == 3 || input1 == 4));

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

            System.out.println(
                    "\n  \033[31mNOTE\033[0m: Please remove the leading zero's to prevent errors (e.g., 0025 --> 25)\n");
            System.out.print("  Enter row number of username/password: ");
            scanner.nextInt();

        } else if (input1 == 3) {

            scanner.nextLine();
            System.out.print("\n  Enter the username you want to add: ");
            String addUsername = scanner.nextLine();

            System.out.print("\n  Enter the password you want to add: ");
            String addPassword = scanner.nextLine();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String algorithm = "\033[31mUser Created\033[0m";
            String format = String.format("%-50s | %-12s | %-19s", addPassword, algorithm, dtf.format(now));

            saveUserPassToDatabase(addUsername, format);

            System.out.println(
                    "\n  \033[32m[i]\033[0m Your username and password are successfuly stored into your vault!\n");

            System.out.print("  Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
            scanner.nextLine();

            printDatabase(args);

        } else {
            main(args);
        }

    }

    public static void saveUserPassToDatabase(String a, String b) {

        tempUserLog.add(a);
        tempPassLog.add(b);
        saveToDatabase();
        tempUserLog.clear();
        tempPassLog.clear();

    }

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
