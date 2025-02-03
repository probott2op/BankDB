 import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

import java.sql.*;
import java.time.LocalDate;

public class App extends Application {

    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Banking System");

        // Database connection
        if (!connectToDatabase()) {
            showAlert("Database Error", "Unable to connect to the database. Exiting application.");
            return;
        }

        // Login Form
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20));
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);
        loginGrid.getStyleClass().add("grid-pane");

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

        Scene loginScene = new Scene(loginGrid, 400, 250);
        loginScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Link CSS file
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    // Connect to MySQL Database
    private boolean connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/BankDB";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
            return false;
        }
    }

    // Authenticate User
    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM login WHERE username = ? AND pass = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
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

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        // Top section with logout button
        HBox topBox = new HBox();
        topBox.setSpacing(10);
        Button logoutButton = new Button("Logout");
        topBox.getChildren().add(logoutButton);
        HBox.setHgrow(topBox, Priority.ALWAYS);
        topBox.setAlignment(Pos.TOP_RIGHT);
        mainLayout.setTop(topBox);

        // Center section with customer details and buttons
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(20));

        // Fetch and display customer details
        String customerDetails = getCustomerDetails(username);
        Text detailsText = new Text(customerDetails);
        detailsText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        // Buttons for actions
        Button viewAccountsButton = new Button("View Accounts");
        Button viewTransactionsButton = new Button("View Transactions");
        Button addCustomerButton = new Button("Add Account");
        Button makeTransactionButton = new Button("Make Transaction");

        centerBox.getChildren().addAll(detailsText, viewAccountsButton, viewTransactionsButton, addCustomerButton, makeTransactionButton);
        mainLayout.setCenter(centerBox);

        // Logout Button Action
        logoutButton.setOnAction(e -> dashboardStage.close());

        // View Accounts Button Action
        viewAccountsButton.setOnAction(e -> {
            String accountsInfo = getAccountsInfo(username);
            showAlert("Accounts", accountsInfo);
        });

        // View Transactions Button Action
        viewTransactionsButton.setOnAction(e -> {
            String transactionsInfo = getTransactionsInfo(username);
            showAlert("Transactions", transactionsInfo);
        });

        // Add Customer Account Button Action
        addCustomerButton.setOnAction(e -> showAddAccountForm(username));

        // Make Transaction Button Action
        makeTransactionButton.setOnAction(e -> showTransactionForm(username));

        Scene dashboardScene = new Scene(mainLayout, 600, 400);
        dashboardScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Link CSS file
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();
    }

    // Fetch Customer Details
    private String getCustomerDetails(String username) {
        String query = "SELECT c.first_name, c.last_name, c.email, c.phone_number, c.address, c.city, c.state, c.zip_code " +
                "FROM customers c " +
                "JOIN login l ON c.customer_id = l.customer_id " +
                "WHERE l.username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "Name: " + rs.getString("first_name") + " " + rs.getString("last_name") + "\n" +
                        "Email: " + rs.getString("email") + "\n" +
                        "Phone: " + rs.getString("phone_number") + "\n" +
                        "Address: " + rs.getString("address") + ", " + rs.getString("city") + ", " + rs.getString("state") + " " + rs.getString("zip_code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Customer details not found.";
    }

    // Fetch Accounts Info
    private String getAccountsInfo(String username) {
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
            return accountsInfo.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to fetch accounts.";
        }
    }

    // Fetch Transactions Info
    private String getTransactionsInfo(String username) {
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
            return transactionsInfo.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to fetch transactions.";
        }
    }

    // Show Add Customer Form
    private void showAddAccountForm(String username) {
        Stage addCustomerStage = new Stage();
        addCustomerStage.setTitle("Add Account");

        GridPane addCustomerGrid = new GridPane();
        addCustomerGrid.setPadding(new Insets(20));
        addCustomerGrid.setVgap(10);
        addCustomerGrid.setHgap(10);

       TextField accountType = new TextField();
        TextField balanceField = new TextField();

        addCustomerGrid.add(new Label("Account Type"), 0, 0);
        addCustomerGrid.add(accountType, 1, 0);
        addCustomerGrid.add(new Label("Initial Balance:"), 0, 1);
        addCustomerGrid.add(balanceField, 1, 1);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String type = accountType.getText();
            double balance = Double.parseDouble(balanceField.getText());

            if (addAccount(username, type, balance)) {
                showAlert("Success", "Account added successfully!");
                addCustomerStage.close();
            } else {
                showAlert("Error", "Failed to add customer.");
            }
        });

        addCustomerGrid.add(saveButton, 1, 2);

        Scene addCustomerScene = new Scene(addCustomerGrid, 400, 400);
        addCustomerStage.setScene(addCustomerScene);
        addCustomerStage.show();
    }

    // Add Customer to Database
    private boolean addAccount(String username, String type,  double balance) {
        String accountQuery = "INSERT INTO accounts (customer_id, account_type, balance, opened_date, status) VALUES (?, ?, ?, ?, ?)";
        String customerQuery = "SELECT l.customer_id FROM login l where l.username = ?";
        try (PreparedStatement accountStmt = connection.prepareStatement(accountQuery);
            PreparedStatement customerStmt = connection.prepareStatement(customerQuery)) {

            customerStmt.setString(1, username);
            ResultSet rs = customerStmt.executeQuery();
            rs.next();
            Long customer_id = rs.getLong("customer_id");

            // Insert account
            accountStmt.setLong(1, customer_id);
            accountStmt.setString(2, type);  
            accountStmt.setDouble(3, balance);
            accountStmt.setDate(4, Date.valueOf(LocalDate.now())); // Set current date
            accountStmt.setString(5, "Active");
            accountStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Show Transaction Form
    private void showTransactionForm(String username) {
        Stage transactionStage = new Stage();
        transactionStage.setTitle("Make Transaction");

        GridPane transactionGrid = new GridPane();
        transactionGrid.setPadding(new Insets(20));
        transactionGrid.setVgap(10);
        transactionGrid.setHgap(10);

        TextField fromAccountField = new TextField();
        TextField toAccountField = new TextField();
        TextField amountField = new TextField();

        transactionGrid.add(new Label("From Account ID:"), 0, 0);
        transactionGrid.add(fromAccountField, 1, 0);
        transactionGrid.add(new Label("To Account ID:"), 0, 1);
        transactionGrid.add(toAccountField, 1, 1);
        transactionGrid.add(new Label("Amount:"), 0, 2);
        transactionGrid.add(amountField, 1, 2);

        Button transferButton = new Button("Transfer");
        transferButton.setOnAction(e -> {
            int fromAccountId = Integer.parseInt(fromAccountField.getText());
            int toAccountId = Integer.parseInt(toAccountField.getText());
            double amount = Double.parseDouble(amountField.getText());

            if (makeTransaction(fromAccountId, toAccountId, amount)) {
                showAlert("Success", "Transaction completed successfully!");
                transactionStage.close();
            } else {
                showAlert("Error", "Failed to complete transaction.");
            }
        });

        transactionGrid.add(transferButton, 1, 3);

        Scene transactionScene = new Scene(transactionGrid, 300, 200);
        transactionStage.setScene(transactionScene);
        transactionStage.show();
    }

    // Make Transaction
    private boolean makeTransaction(int fromAccountId, int toAccountId, double amount) {
        String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_id = ?";
        String updateBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        String insertTransactionQuery = "INSERT INTO transactions (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?)";

        try {
            // Check if the fromAccount has sufficient balance
            PreparedStatement checkBalanceStmt = connection.prepareStatement(checkBalanceQuery);
            checkBalanceStmt.setInt(1, fromAccountId);
            ResultSet rs = checkBalanceStmt.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance < amount) {
                    showAlert("Error", "Insufficient balance.");
                    return false;
                }
            }

            // Deduct amount from the fromAccount
            PreparedStatement deductStmt = connection.prepareStatement(updateBalanceQuery);
            deductStmt.setDouble(1, -amount);
            deductStmt.setInt(2, fromAccountId);
            deductStmt.executeUpdate();

            // Add amount to the toAccount
            PreparedStatement addStmt = connection.prepareStatement(updateBalanceQuery);
            addStmt.setDouble(1, amount);
            addStmt.setInt(2, toAccountId);
            addStmt.executeUpdate();

            // Record the transaction
            PreparedStatement transactionStmt = connection.prepareStatement(insertTransactionQuery);
            transactionStmt.setInt(1, fromAccountId);
            transactionStmt.setString(2, "Withdrawal");
            transactionStmt.setDouble(3, amount);
            transactionStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            transactionStmt.executeUpdate();

            transactionStmt.setInt(1, toAccountId);
            transactionStmt.setString(2, "Deposit");
            transactionStmt.setDouble(3, amount);
            transactionStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            transactionStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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