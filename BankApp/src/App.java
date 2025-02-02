import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class App extends Application {

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banking System");

        // Database connection
        connectToDatabase();

        // Login Form
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(10, 10, 10, 10));
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Label statusLabel = new Label();

        loginGrid.add(userLabel, 0, 0);
        loginGrid.add(userField, 1, 0);
        loginGrid.add(passLabel, 0, 1);
        loginGrid.add(passField, 1, 1);
        loginGrid.add(loginButton, 1, 2);
        loginGrid.add(statusLabel, 1, 3);

        // Login Button Action
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (authenticateUser(username, password)) {
                statusLabel.setText("Login Successful!");
                showCustomerDashboard(username);
            } else {
                statusLabel.setText("Invalid username or password.");
            }
        });

        Scene loginScene = new Scene(loginGrid, 300, 200);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Connect to MySQL Database
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/BankDB";
            String user = "root"; 
            String password = ""; 
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticate User
    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM login WHERE username = ? AND password_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // In a real app, hash the password before comparing
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a match is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Show Customer Dashboard
    private void showCustomerDashboard(String username) {
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Customer Dashboard");

        GridPane dashboardGrid = new GridPane();
        dashboardGrid.setPadding(new Insets(10, 10, 10, 10));
        dashboardGrid.setVgap(10);
        dashboardGrid.setHgap(10);

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        Button viewAccountsButton = new Button("View Accounts");
        Button viewTransactionsButton = new Button("View Transactions");
        Button logoutButton = new Button("Logout");

        dashboardGrid.add(welcomeLabel, 0, 0);
        dashboardGrid.add(viewAccountsButton, 0, 1);
        dashboardGrid.add(viewTransactionsButton, 0, 2);
        dashboardGrid.add(logoutButton, 0, 3);

        // View Accounts Button Action
        viewAccountsButton.setOnAction(e -> {
            String query = "SELECT a.account_id, a.account_type, a.balance FROM accounts a " +
                    "JOIN customers c ON a.customer_id = c.customer_id " +
                    "JOIN login l ON c.customer_id = l.customer_id " +
                    "WHERE l.username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                StringBuilder accountsInfo = new StringBuilder("Accounts:\n");
                while (rs.next()) {
                    accountsInfo.append("Account ID: ").append(rs.getInt("account_id"))
                            .append(", Type: ").append(rs.getString("account_type"))
                            .append(", Balance: ").append(rs.getDouble("balance"))
                            .append("\n");
                }
                showAlert("Accounts", accountsInfo.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // View Transactions Button Action
        viewTransactionsButton.setOnAction(e -> {
            String query = "SELECT t.transaction_id, t.transaction_type, t.amount, t.transaction_date " +
                    "FROM transactions t " +
                    "JOIN accounts a ON t.account_id = a.account_id " +
                    "JOIN customers c ON a.customer_id = c.customer_id " +
                    "JOIN login l ON c.customer_id = l.customer_id " +
                    "WHERE l.username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                StringBuilder transactionsInfo = new StringBuilder("Transactions:\n");
                while (rs.next()) {
                    transactionsInfo.append("Transaction ID: ").append(rs.getInt("transaction_id"))
                            .append(", Type: ").append(rs.getString("transaction_type"))
                            .append(", Amount: ").append(rs.getDouble("amount"))
                            .append(", Date: ").append(rs.getTimestamp("transaction_date"))
                            .append("\n");
                }
                showAlert("Transactions", transactionsInfo.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Logout Button Action
        logoutButton.setOnAction(e -> dashboardStage.close());

        Scene dashboardScene = new Scene(dashboardGrid, 300, 200);
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();
    }

    // Show Alert Dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}