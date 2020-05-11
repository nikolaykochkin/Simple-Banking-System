package banking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String fileName;

    public Database(String fileName) {
        this.fileName = fileName;
        createSchema();
    }

    public void createSchema() {

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " number TEXT,\n"
                + " pin TEXT,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Statement stmt = Connect.getConnection(fileName).createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCard(Account account) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";

        try (PreparedStatement pstmt = Connect.getConnection(fileName).prepareStatement(sql)) {
            pstmt.setString(1, account.getCardNumber());
            pstmt.setString(2, account.getPinCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Account getCard(String cardNumber) {
        String sql = "SELECT number, pin, balance "
                + "FROM card WHERE number = ?";

        try (PreparedStatement pstmt = Connect.getConnection(fileName).prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getString("number"),
                        rs.getString("pin"),
                        rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addIncome(Account account, int sum) {
        String sql = "UPDATE card "
                + "SET balance = balance + ? "
                + "WHERE number = ?";

        try (PreparedStatement pstmt = Connect.getConnection(fileName).prepareStatement(sql)) {
            pstmt.setInt(1, sum);
            pstmt.setString(2, account.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeAccount(Account account) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (PreparedStatement pstmt = Connect.getConnection(fileName).prepareStatement(sql)) {
            pstmt.setString(1, account.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doTransfer(Account accountFrom, Account accountTo, int sum) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (PreparedStatement pstmt = Connect.getConnection(fileName).prepareStatement(sql)) {
            pstmt.setInt(1, -sum);
            pstmt.setString(2, accountFrom.getCardNumber());
            pstmt.addBatch();
            pstmt.setInt(1, sum);
            pstmt.setString(2, accountTo.getCardNumber());
            pstmt.addBatch();
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
