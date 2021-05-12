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

    static String file = "Accounts.txt";
    static String keyB;
    static String usr;
    static boolean alreadyExecuted = false;
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        testing(args);
        saveToDatabase();
        clearScreen();
        startupMenu(args);

    }

    // Saves Data from Database to textfile
    public static void saveToDatabase() {

        try {

            File database = new File(String.format("%s's_Personal_Database.txt", usr));
            FileWriter fw;

            if (!database.exists()) {

                fw = new FileWriter(database);

            } else {

                fw = new FileWriter(database, true);

                for (int i = 0; i < tempUserLog.size(); i++) {
                    String tempVar = encrypt(tempUserLog.get(i), keyB) + "\n" + encrypt(tempPassLog.get(i), keyB)
                            + "\n";
                    fw.write(tempVar);
                }

                fw.close();

            }

        } catch (IOException e) {
            System.out.println("\n An error occurred!");
            e.printStackTrace();
        }

    }

    public static void setup(String[] args) {

        clearScreen();
        banner();
        System.out.println("\n [i] \033[32mSetup Account\033[0m \n -----------------");

        System.out.print("\n Setup your username: ");
        String tempA = scan.nextLine();

        System.out.print("\n Setup your password for your vault: ");
        keyB = scan.nextLine();

        String filename = String.format("%s's_Personal_Database.txt", tempA);

        try {

            File B = new File(file);
            FileWriter A = new FileWriter(filename);
            FileWriter fw = new FileWriter(B, true);
            fw.write(encrypt(tempA, nullA) + "\n");
            fw.write(encrypt(keyB, nullA) + "\n");
            fw.close();
            main(args);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void login(String[] args) {

        try {

            File f = new File(file);
            BufferedReader reader;
            Scanner fileScanner = new Scanner(f);

            if (f.length() == 0) {
                System.out.println("\n There are no existing accounts! Please make one.");
                System.out.print("\n Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
                scan.nextLine();
                testing(args);
            }

            String line;
            int n = 0;

            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {

                if (n % 2 == 0) {
                    tempUserLog.add(decrypt(line, nullA));
                } else {
                    tempPassLog.add(decrypt(line, nullA));
                }
                n++;
            }

            reader.close();

            fileScanner.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Console c = System.console();
        clearScreen();
        banner();
        System.out.println("\n [i] \033[32mLogin Screen\033[0m \n ----------------");

        System.out.print("\n Enter your username: ");
        String username = scan.nextLine();

        System.out.print("\n Enter your password: ");
        char[] temp = c.readPassword();
        String password = String.valueOf(temp);

        boolean usernameExists = false;
        boolean passwordExists = false;

        for (int a = 0; a < tempUserLog.size(); a++) {
            if (username.equals(tempUserLog.get(a))) {
                usernameExists = true;
            }
            if (password.equals(tempPassLog.get(a))) {
                passwordExists = true;
            }
        }

        tempUserLog.clear();
        tempPassLog.clear();

        if (usernameExists == false || passwordExists == false) {
            System.out.println("\n Username or password is either incorrect or not existing!");
            System.out.print("\n Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
            scan.nextLine();
            clearScreen();
            banner();
            testing(args);
        } else {
            usr = username;
            keyB = password;
        }

    }

    static String nullA = WordList.keyA;
    public static void testing(String[] args) {

        try {
            FileWriter fw = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clearScreen();
        banner();
        System.out.println("\n [i] \033[32mWelcome!\033[0m\n ────────────");

        System.out.println("\n [1] Login Account");
        System.out.println(" [2] Create an account");

        System.out.print("\n Choose (Ctrl + C to exit): ");
        int tempA;

        do {

            while (!scan.hasNextInt()) {
                String input = scan.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print("\n Choose (Ctrl + C to exit): ");
            }

            tempA = scan.nextInt();
            scan.nextLine();

            if (tempA == 1) {
                login(args);
            } else if (tempA == 2) {
                setup(args);
            } else {
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", tempA);
                System.out.print("\n Choose (Ctrl + C to exit): ");
            }

        } while (!(tempA == 1 || tempA == 2));

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void startupMenu(String[] args) {

        banner();
        System.out.println("\n [i] \033[32mMain menu\033[0m \n ─────────────\n");

        System.out.println(" [1] Generate random Username/Password");
        System.out.println(" [2] View/Manage your vault \033[32m(Password Protected)\033[0m");
        System.out.println(" [3] Program info \033[31m[?]\033[0m");
        System.out.println(" [4] Logout");

        System.out.print("\n Choose (Ctrl + C to exit): ");
        int input1;

        do {

            while (!scan.hasNextInt()) {
                String input = scan.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print("\n Choose (Ctrl + C to exit): ");
            }

            input1 = scan.nextInt();

            if (input1 == 1) {
                generateUsername_Password(args);
            } else if (input1 == 2) {
                printDatabase(args);
            } else if (input1 == 3) {

                System.out.println("\n [?] \033[32mProgram info\033[0m");
                System.out.println("\n Program: Password Manager");
                System.out.println(" Version: BETA");
                System.out.println(" Github: https://github.com/FrancisIGP");
                System.out.println(" Developer: FrancisIGP");

                scan.nextLine();
                System.out.print("\n Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
                scan.nextLine();

                clearScreen();
                startupMenu(args);

            } else if (input1 == 4) {
                scan.nextLine();
                main(args);
            } else {
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input1);
                System.out.print("\n Choose (Ctrl + C to exit): ");
            }

        } while (!(input1 == 1 || input1 == 2 || input1 == 3 || input1 == 4));

        scan.close();

    }

    public static void generateUsername_Password(String[] args) {

        WordList u = new WordList();
        SecureRandom sr = new SecureRandom();

        System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");

        int psdAmtChar;

        do {

            while (!scan.hasNextInt()) {
                String input = scan.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");
            }

            psdAmtChar = scan.nextInt();

            if (!(psdAmtChar >= 8 && psdAmtChar <= 50)) {
                System.out.printf("\n (\033[31mError\033[0m): Not in range! \"%s\"\n", psdAmtChar);
                System.out.print("\n How many characters do you want for your password (8 min, 50 max)? ");
            }

        } while (!(psdAmtChar >= 8 && psdAmtChar <= 50));

        scan.nextLine();

        do {

            clearScreen();
            banner();

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

            System.out.println("\n \033[31m[i]\033[0m We've successfully generated your username and password!\n");
            String strPassword = String.valueOf(password);

            temp = String.format("%-50s | %-12s | %-19s", strPassword, sr.getAlgorithm(), dtf.format(now));

            System.out.println("  \033[32mGenerated Username/Password\033[0m\n  ───────────────────────────\n");
            System.out.println("  +───────────+────────────────────────────────────────────────────+");
            System.out.printf("  | Username: | %-50s |\n", username);
            System.out.println("  +───────────+────────────────────────────────────────────────────+");
            System.out.printf("  | Password: | %-50s |\n", strPassword);
            System.out.println("  +───────────+────────────────────────────────────────────────────+");

            do {

                System.out.print("\n Do you want to claim and save this username/password (Y[yes]/N[no]/Q[quit])? ");
                choice = scan.nextLine();

            } while (!(choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("Q")));

            if (choice.equalsIgnoreCase("Y")) {

                saveUserPassToDatabase(username, temp);
                System.out.println(
                        "\n \033[32m[i]\033[0m Your username and password are successfuly stored into your vault!\n");
                break;

            } else if (choice.equalsIgnoreCase("N")) {
                continue;
            } else {
                clearScreen();
                startupMenu(args);
                break;
            }

        } while (true);

        System.out.print(" Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
        scan.nextLine();

        clearScreen();
        startupMenu(args);
        System.out.println("\n");

        scan.close();

    }

    public static void printDatabase(String[] args) {

        File database = new File(String.format("%s's_Personal_Database.txt", usr));
        Console c = System.console();

        BufferedReader reader;
        String cp = "";

        if (alreadyExecuted == false) {

            for (int psdTrials = 0, trialsLeft = 3; psdTrials < 3;) {

                System.out.printf("\n Enter your password (Trials left: \033[31m%d\033[0m): ", trialsLeft);
                char[] ap = c.readPassword();
                cp = String.valueOf(ap);

                if (!cp.equalsIgnoreCase(keyB)) {
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
        banner();
        System.out.println("\n  [i] \033[32mDatabase Options\033[0m\n  ────────────────────\n");

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

            reader = new BufferedReader(new FileReader(String.format("%s's_Personal_Database.txt", usr)));

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

                databasePrompt(args);
                main(args);

            } else {

                String line;
                int n = 0;
                try {

                    while ((line = reader.readLine()) != null) {

                        if (n % 2 == 0) {
                            tempUserLog.add(decrypt(line, keyB));
                        } else {
                            tempPassLog.add(decrypt(line, keyB));
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
                databasePrompt(args);

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

    }

    public static void databasePrompt(String[] args) {

        System.out.print(" \n  Choose: ");
        int input1;

        do {

            while (!scan.hasNextInt()) {
                String input = scan.next();
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input);
                System.out.print(" \n Choose: ");
            }

            input1 = scan.nextInt();

            if (!(input1 == 1 || input1 == 2 || input1 == 3 || input1 == 4)) {
                System.out.printf("\n (\033[31mError\033[0m): Invalid Input! \"%s\"\n", input1);
                System.out.print(" \n Choose: ");
            }

        } while (!(input1 == 1 || input1 == 2 || input1 == 3 || input1 == 4));

        if (input1 == 1) {
            try {
                FileWriter clearLog = new FileWriter(String.format("%s's_Personal_Database.txt", usr));
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
            scan.nextInt();

        } else if (input1 == 3) {

            scan.nextLine();
            System.out.print("\n  Enter the username you want to add: ");
            String addUsername = scan.nextLine();

            System.out.print("\n  Enter the password you want to add: ");
            String addPassword = scan.nextLine();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String algorithm = "\033[31mUser Created\033[0m";
            String format = String.format("%-50s | %-12s | %-19s", addPassword, algorithm, dtf.format(now));

            saveUserPassToDatabase(addUsername, format);

            System.out.println(
                    "\n  \033[32m[i]\033[0m Your username and password are successfuly stored into your vault!\n");

            System.out.print("  Press \033[32m[ENTER]\033[0m to continue (Ctrl + C to exit)...");
            scan.nextLine();

            printDatabase(args);

        } else {
            clearScreen();
            startupMenu(args);
        }

    }

    public static void saveUserPassToDatabase(String a, String b) {

        tempUserLog.add(a);
        tempPassLog.add(b);
        saveToDatabase();
        tempUserLog.clear();
        tempPassLog.clear();

    }

    public static void banner() {
        System.out.println("\n \033[33m█▀█ ▄▀█ █▀ █▀ █ █ █ █▀█ █▀█ █▀▄   █▀▄▀█ ▄▀█ █▄ █ ▄▀█ █▀▀ █▀▀ █▀█\033[0m");
        System.out.println(" \033[33m█▀▀ █▀█ ▄█ ▄█ ▀▄▀▄▀ █▄█ █▀▄ █▄▀   █ ▀ █ █▀█ █ ▀█ █▀█ █▄█ ██▄ █▀▄\033[0m");
        System.out.println(" [\033[31mDeveloper\033[0m: FrancisIGP || \033[31mVersion\033[0m: Beta]");
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

}
