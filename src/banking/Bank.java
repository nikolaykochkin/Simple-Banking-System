package banking;

import java.util.Arrays;
import java.util.Random;

public class Bank {
    private final Random random;
    private final Database database;

    public Bank(Database database) {
        this.database = database;
        random = new Random();
    }

    public Account createNewAccount() {
        String cardNumber = generateCardNumber();
        while (database.getCard(cardNumber) != null) {
            cardNumber = generateCardNumber();
        }
        String pin = ("000" + random.nextInt(9999));
        Account account = new Account(cardNumber, pin.substring(pin.length() - 4), 0);
        database.insertCard(account);
        return account;
    }

    public Account login(String cardNumber, String pin) {
        Account account = database.getCard(cardNumber);
        if (account != null && account.getPinCode().equals(pin)) {
            return account;
        } else {
            return null;
        }
    }

    private String generateCardNumber() {
        String prefix = "400000";
        String number = "000000000" + random.nextInt(999999999);
        String cardNumber = prefix + number.substring(number.length() - 9);
        return (cardNumber + calculateCheckSum(cardNumber));
    }

    public static String calculateCheckSum(String cardNumber) {
        int[] digits = Arrays.stream(cardNumber.split(""))
                .limit(15)
                .mapToInt(Integer::parseInt)
                .toArray();
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            int digit = digits[i];
            if (i % 2 == 0) {
                digit *= 2;
                digit = digit > 9 ? digit - 9 : digit;
            }
            sum += digit;
        }
        return sum % 10 == 0 ? "0" : String.valueOf(10 - sum % 10);
    }

    public void addIncome(Account account, int sum) {
        database.addIncome(account, sum);
        account.setBalance(account.getBalance() + sum);
        System.out.println("\nYou have successfully added income!");
    }

    public void doTransfer(Account account, String cardNumber, int sum) {
        if (cardNumber.equals(account.getCardNumber())) {
            System.out.println("\nYou can't transfer money to the same account!");
            return;
        }

        if (!cardNumber.endsWith(calculateCheckSum(cardNumber))) {
            System.out.println("\nProbably you made mistake in card number. Please try again!");
            return;
        }

        Account accountTo = database.getCard(cardNumber);
        if (accountTo == null) {
            System.out.println("\nSuch a card does not exist.");
            return;
        }

        database.doTransfer(account, accountTo, sum);
        account.setBalance(account.getBalance() - sum);
        System.out.println("\nYou have successfully had transfer!");
    }

    public void closeAccount(Account account) {
        database.closeAccount(account);
        System.out.println("\nYou have successfully closed account!");
    }
}
