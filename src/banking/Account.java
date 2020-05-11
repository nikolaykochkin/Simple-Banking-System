package banking;

import java.util.Scanner;

public class Account {
    private final String cardNumber;
    private final String pinCode;
    private int balance;

    public Account(String cardNumber, String pinCode, int balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
    }

    public int start(Bank bank) {
        boolean active = true;
        int sum;
        Scanner scanner = new Scanner(System.in);
        while (active) {
            System.out.println("\n1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit");
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    System.out.println(balance);
                    break;
                case "2":
                    System.out.println("\nEnter income sum:");
                    sum = Integer.parseInt(scanner.nextLine());
                    bank.addIncome(this, sum);
                    break;
                case "3":
                    System.out.println("\nEnter transfer card number:");
                    String cardNumber = scanner.nextLine();
                    System.out.println("Enter sum of transfer:");
                    sum = Integer.parseInt(scanner.nextLine());
                    bank.doTransfer(this, cardNumber, sum);
                    break;
                case "4":
                    bank.closeAccount(this);
                    active = false;
                    break;
                case "5":
                    active = false;
                    break;
                case "0":
                    return 0;
                default:
                    System.out.println("Wrong command");
            }
        }
        return 1;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Your card number:\n" + cardNumber +
                "\nYour card PIN:\n" + pinCode;
    }
}
