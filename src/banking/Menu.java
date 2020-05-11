package banking;

import java.util.Scanner;

public class Menu {
    private final Bank bank;
    private boolean active;

    public Menu(Bank bank) {
        this.bank = bank;
        active = true;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (active) {
            System.out.println("\n1. Create account\n2. Log into account\n0. Exit");
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    login(scanner);
                    break;
                case "0":
                    active = false;
                    break;
                default:
                    System.out.println("Wrong command");
            }
        }
        System.out.println("\nBye!");
    }

    private void createAccount() {
        Account account = bank.createNewAccount();
        System.out.println("\nYour card have been created");
        System.out.println(account);
    }

    private void login(Scanner scanner) {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        Account account = bank.login(cardNumber, pin);
        if (account == null) {
            System.out.println("\nWrong card number or PIN!");
            return;
        }

        System.out.println("\nYou have successfully logged in!");
        int exitCode = account.start(bank);
        active = exitCode != 0;
    }

}
