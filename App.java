import java.util.Arrays;
import java.util.Scanner;

public class App {
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final String clear = "\033[H\033[2J";
        final String color_Blue = "\033[1;34m", color_green = "\033[1;32m", Purple = "\033[1;35m", reset = "\033[0m",
                Yellow = "\033[1;33m", GREEN_BACKGROUND = "\033[42m", color_red = "\033[1;31m";

        final String Dashboard = "Smart Banking System";
        final String Open_New_Acc = "Open New Account";
        final String Deposit_Money = "Deposit Money";
        final String WithDraw = "Withdraw Money";
        final String Transfer_Money = "Transfer Money";
        final String Acc_Bal = "Check Account Balance";
        final String Del_Acc = "Delete Account";

        final String ERROR_MESSAGE = String.format("%s%s%s", color_red, "%s", reset);
        final String SUCCESS_MESSAGE = String.format("%s%s%s", color_green, "%s", reset);

        String screen = Dashboard;

        // Arrays
        String[][] details = new String[0][3];

        main_loop: do {
            // Title
            title(screen);

            lbl_main: switch (screen) {

                case Dashboard:
                    System.out.println("[1]. Open New Account");
                    System.out.println("[2]. Deposit Money");
                    System.out.println("[3]. Withdraw Money");
                    System.out.println("[4]. Transfer Money");
                    System.out.println("[5]. Check Account Balance");
                    System.out.println("[6]. Delete Account");
                    System.out.println("[7]. Exit");
                    System.out.print("Enter an Option to Continue >> ");
                    int option = scanner.nextInt();
                    scanner.nextLine();

                    switch (option) {
                        case 1:
                            screen = Open_New_Acc;
                            break;
                        case 2:
                            screen = Deposit_Money;
                            break;
                        case 3:
                            screen = WithDraw;
                            break;
                        case 4:
                            screen = Transfer_Money;
                            break;
                        case 5:
                            screen = Acc_Bal;
                            break;
                        case 6:
                            screen = Del_Acc;
                            break;
                        case 7:
                            System.exit(0);
                            break;
                        // default : continue;
                    }
                    break;

                case Open_New_Acc:
                    // Generate Acc number
                    boolean validName = false;
                    int x = 1;
                    double initDepo;
                    String id, accName;
                    loop_name: while (true) {
                        id = String.format("SDB-%05d", x);
                        System.out.printf("New Account number => %s%s%s\n", Purple, id, reset);
                        System.out.print("Enter name: ");
                        accName = scanner.nextLine().strip();
                        // name validation
                        if (!phraseValidate(accName, "Name")) {
                            System.out.print("Do you want to try again(y/n) >> ");
                            if (scanner.nextLine().strip().toUpperCase().equals("Y"))
                                continue loop_name;

                            screen = Dashboard;
                            break lbl_main;
                        }
                        // Initial Desposit
                        do {
                            System.out.print("Initial Deposit: ");
                            initDepo = scanner.nextDouble();
                            scanner.nextLine();
                            if (initDepo < 5000) {
                                System.out.print("Insufficient Deposit.Do you want Deposit sufficient amount(Y/N): ");
                                if (scanner.nextLine().strip().toUpperCase().equals("Y"))
                                    continue;
                                else
                                    screen = Dashboard;
                                break loop_name;

                            } else {
                                System.out.printf("Account number %s%s%s of %s\033[1;30m%s%s has been created\n",
                                        Yellow, id, reset, GREEN_BACKGROUND, accName.toUpperCase(), reset);
                                break;
                            }
                        } while (true);

                        // Store details
                        details = addNewAccount(details, id, accName, initDepo);

                        for (int i = 0; i < details.length; i++) {
                            System.out.println(Arrays.toString(details[i]));
                        }

                        System.out.print("Do you want to Open another Account? (Y/N) >> ");

                        if (scanner.nextLine().strip().toUpperCase().equals("Y")) {
                            x++;
                            continue;
                        } else
                            screen = Dashboard;
                        break lbl_main;

                    }

                case Deposit_Money:
                    do {
                        System.out.print("Enter account number: ");
                        String accountNumber = scanner.nextLine();

                        if (accountNumberValidation(accountNumber, details)) {
                            System.out.printf("Current Balance: %f\n", findBalance(accountNumber, details));
                            System.out.print("Deposite Amount: ");
                            double deposite = scanner.nextDouble();
                            scanner.nextLine();

                            if (deposite > 500) {
                                details = updateBalance(accountNumber, deposite, details);
                                System.out.printf("New Balance: %f\n", findBalance(accountNumber, details));
                            } else {
                                System.out.print("Do you want to do another deposite(Y/N): ");
                                if (scanner.nextLine().strip().toUpperCase().equals("Y"))
                                    continue;
                                else {
                                    screen = Dashboard;
                                    break lbl_main;
                                }

                            }
                            System.out.print("Do you want to do another deposite(Y/N): ");
                            if (scanner.nextLine().strip().toUpperCase().equals("Y"))
                                continue;
                            else {
                                screen = Dashboard;
                                break lbl_main;
                            }

                        } else {
                            System.out.print("Do you want to try again(Y/N): ");
                            if (scanner.nextLine().strip().toUpperCase().equals("Y"))
                                continue;
                            else {
                                screen = Dashboard;
                                break lbl_main;
                            }
                        }
                    } while (true);

            }
        } while (true);
    }

    public static void title(String screen) {
        final String clear = "\033[H\033[2J";
        final String color_Blue = "\033[1;34m", color_green = "\033[1;32m", reset = "\033[0m";

        System.out.print(clear);
        String line = String.format("%s%s%s", color_green, "-".repeat(60), reset);
        String title = String.format("%s%s%s%s", color_Blue, " ".repeat((60 - screen.length()) / 2),
                screen.toUpperCase(), reset);
        System.out.println(line);
        System.out.println(title);
        System.out.println(line);
    }

    public static boolean phraseValidate(String phrase, String name) {
        final String reset = "\033[0m", color_red = "\033[1;31m";
        final String ERROR_MESSAGE = String.format("%s%s%s", color_red, "%s", reset);
        if (phrase.strip().isBlank()) {
            String err = String.format(ERROR_MESSAGE, name.concat(" Cannot be empty."));
            System.out.println(err);
            return false;
        }

        for (int i = 0; i < phrase.length(); i++) {
            if (!(Character.isLetter(phrase.charAt(i)) || Character.isSpaceChar(phrase.charAt(i)))) {
                String err = String.format(ERROR_MESSAGE, name.concat(" contains invalid character."));
                System.out.println(err);
                return false;
            }
        }

        return true;
    }

    public static String[][] addNewAccount(String[][] details, String accountNumber, String name, double deposit) {
        String[][] newDetails = new String[details.length + 1][3];
        for (int i = 0; i < details.length; i++) {
            newDetails[i] = details[i];
        }
        newDetails[newDetails.length - 1][0] = accountNumber;
        newDetails[newDetails.length - 1][1] = name;
        newDetails[newDetails.length - 1][2] = String.format("%.2f", deposit);

        return newDetails;

    }

    public static boolean accountNumberValidation(String accountNumber, String[][] details) {
        final String reset = "\033[0m", color_red = "\033[1;31m";
        final String ERROR_MESSAGE = String.format("%s%s%s", color_red, "%s", reset);
        if (accountNumber.strip().isBlank()) {
            String err = String.format(ERROR_MESSAGE, "Account number Cannot be empty.");
            System.out.println(err);
            return false;
        }

        if (accountNumber.length() != 9 || !accountNumber.startsWith("SDB-")) {
            String err = String.format(ERROR_MESSAGE, "Invalid Format");
            System.out.println(err);
            return false;
        }

        String numberPart = accountNumber.substring(4);
        for (int i = 0; i < numberPart.length(); i++) {
            if (!Character.isDigit(numberPart.charAt(i))) {
                String err = String.format(ERROR_MESSAGE, "Account number contains invalid character");
                System.out.println(err);
                return false;
            }
        }

        for (int i = 0; i < details.length; i++) {
            if (accountNumber.equals(details[i][0])) {
                return true;
            }
        }

        String err = String.format(ERROR_MESSAGE, "Account number not found");
        System.out.println(err);
        return false;

    }

    public static String[][] updateBalance(String accountNumber, double deposite, String[][] details) {
        for (int i = 0; i < details.length; i++) {
            if (accountNumber.equals(details[i][0])) {
                double newBalnce = Double.valueOf(details[i][2]) + deposite;
                details[i][2] = String.format("%.2f", newBalnce);
                return details;
            }
        }
        return null;
    }

    public static double findBalance(String accountNumber, String[][] details) {
        for (int i = 0; i < details.length; i++) {
            if (accountNumber.equals(details[i][0])) {
                return Double.valueOf(details[i][2]);
            }
        }
        return -1;
    }
}