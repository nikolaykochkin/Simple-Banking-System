package banking;

public class Main {

    public static void main(String[] args) {
        String fileName = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName")) {
                fileName = args[i + 1];
            }
        }
        Database db = new Database(fileName);
        Bank bank = new Bank(db);
        Menu menu = new Menu(bank);
        menu.start();
        Connect.closeConnection();
    }
}
