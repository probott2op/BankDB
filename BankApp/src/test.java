/* import javafx.application.Application;
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
        dashboardGrid.setPadding(new Insets(20));
        dashboardGrid.setVgap(10);
        dashboardGrid.setHgap(10);
        dashboardGrid.getStyleClass().add("dashboard-grid");

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

        Scene dashboardScene = new Scene(dashboardGrid, 400, 250);
        dashboardScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Link CSS file
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
} */
/* 
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
        Button addCustomerButton = new Button("Add Customer");
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

        // Add Customer Button Action
        addCustomerButton.setOnAction(e -> showAddCustomerForm());

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
    private void showAddCustomerForm() {
        Stage addCustomerStage = new Stage();
        addCustomerStage.setTitle("Add Customer");

        GridPane addCustomerGrid = new GridPane();
        addCustomerGrid.setPadding(new Insets(20));
        addCustomerGrid.setVgap(10);
        addCustomerGrid.setHgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField cityField = new TextField();
        TextField stateField = new TextField();
        TextField zipField = new TextField();
        TextField balanceField = new TextField();

        addCustomerGrid.add(new Label("First Name:"), 0, 0);
        addCustomerGrid.add(firstNameField, 1, 0);
        addCustomerGrid.add(new Label("Last Name:"), 0, 1);
        addCustomerGrid.add(lastNameField, 1, 1);
        addCustomerGrid.add(new Label("Email:"), 0, 2);
        addCustomerGrid.add(emailField, 1, 2);
        addCustomerGrid.add(new Label("Phone:"), 0, 3);
        addCustomerGrid.add(phoneField, 1, 3);
        addCustomerGrid.add(new Label("Address:"), 0, 4);
        addCustomerGrid.add(addressField, 1, 4);
        addCustomerGrid.add(new Label("City:"), 0, 5);
        addCustomerGrid.add(cityField, 1, 5);
        addCustomerGrid.add(new Label("State:"), 0, 6);
        addCustomerGrid.add(stateField, 1, 6);
        addCustomerGrid.add(new Label("Zip Code:"), 0, 7);
        addCustomerGrid.add(zipField, 1, 7);
        addCustomerGrid.add(new Label("Initial Balance:"), 0, 8);
        addCustomerGrid.add(balanceField, 1, 8);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String zip = zipField.getText();
            double balance = Double.parseDouble(balanceField.getText());

            if (addCustomer(firstName, lastName, email, phone, address, city, state, zip, balance)) {
                showAlert("Success", "Customer added successfully!");
                addCustomerStage.close();
            } else {
                showAlert("Error", "Failed to add customer.");
            }
        });

        addCustomerGrid.add(saveButton, 1, 9);

        Scene addCustomerScene = new Scene(addCustomerGrid, 400, 400);
        addCustomerStage.setScene(addCustomerScene);
        addCustomerStage.show();
    }

    // Add Customer to Database
    private boolean addCustomer(String firstName, String lastName, String email, String phone, String address, String city, String state, String zip, double balance) {
        String customerQuery = "INSERT INTO customers (first_name, last_name, email, phone_number, address, city, state, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String accountQuery = "INSERT INTO accounts (customer_id, account_type, account_number, balance, opened_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement customerStmt = connection.prepareStatement(customerQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement accountStmt = connection.prepareStatement(accountQuery)) {

            // Insert customer
            customerStmt.setString(1, firstName);
            customerStmt.setString(2, lastName);
            customerStmt.setString(3, email);
            customerStmt.setString(4, phone);
            customerStmt.setString(5, address);
            customerStmt.setString(6, city);
            customerStmt.setString(7, state);
            customerStmt.setString(8, zip);
            customerStmt.executeUpdate();

            // Get the generated customer ID
            ResultSet rs = customerStmt.getGeneratedKeys();
            int customerId = 0;
            if (rs.next()) {
                customerId = rs.getInt(1);
            }

            // Insert account
            accountStmt.setInt(1, customerId);
            accountStmt.setString(2, "Savings"); // Default account type
            accountStmt.setString(3, "ACC" + customerId); // Generate account number
            accountStmt.setDouble(4, balance);
            accountStmt.setDate(5, Date.valueOf(LocalDate.now())); // Set current date
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
        String insertTransactionQuery = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?)";

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
            transactionStmt.executeUpdate();

            transactionStmt.setInt(1, toAccountId);
            transactionStmt.setString(2, "Deposit");
            transactionStmt.setDouble(3, amount);
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
 */