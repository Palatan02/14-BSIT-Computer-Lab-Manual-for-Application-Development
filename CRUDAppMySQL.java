import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class CRUDAppMySQL extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField searchField;
    private JTextArea displayArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CRUDAppMySQL().initializeGUI());
    }

    private void initializeGUI() {
        setTitle("Laboratory Manual - Database Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Database Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(centerPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        formPanel.add(new JLabel("Search Name:"));
        searchField = new JTextField();
        formPanel.add(searchField);
        centerPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton addButton = new JButton("Add");
        JButton showButton = new JButton("Show");
        JButton searchButton = new JButton("Search");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> addUser());
        showButton.addActionListener(e -> showRecords());
        searchButton.addActionListener(e -> searchUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());

        buttonPanel.add(addButton);
        buttonPanel.add(showButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        centerPanel.add(buttonPanel);

        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        centerPanel.add(scrollPane);

        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3000/myoop", "root", "1234");
    }

    private void addUser() {
        try (Connection con = getConnection()) {
            String query = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.executeUpdate();
            displayArea.append("User added successfully.\n");
        } catch (Exception e) {
            displayArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    private void showRecords() {
        try (Connection con = getConnection()) {
            String query = "SELECT * FROM users";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            displayArea.setText("");
            while (rs.next()) {
                displayArea.append("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Email: " + rs.getString("email") + "\n");
            }
        } catch (Exception e) {
            displayArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    private void searchUser() {
        try (Connection con = getConnection()) {
            String query = "SELECT * FROM users WHERE name LIKE ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + searchField.getText() + "%");
            ResultSet rs = ps.executeQuery();
            displayArea.setText("");
            while (rs.next()) {
                displayArea.append("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Email: " + rs.getString("email") + "\n");
            }
        } catch (Exception e) {
            displayArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    private void updateUser() {
        try (Connection con = getConnection()) {
            String query = "UPDATE users SET email = ? WHERE name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, emailField.getText());
            ps.setString(2, searchField.getText());
            int result = ps.executeUpdate();
            displayArea.append(result > 0 ? "User updated successfully.\n" : "User not found.\n");
        } catch (Exception e) {
            displayArea.append("Error: " + e.getMessage() + "\n");
        }
    }

    private void deleteUser() {
        try (Connection con = getConnection()) {
            String query = "DELETE FROM users WHERE name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, searchField.getText());
            int result = ps.executeUpdate();
            displayArea.append(result > 0 ? "User deleted successfully.\n" : "User not found.\n");
        } catch (Exception e) {
            displayArea.append("Error: " + e.getMessage() + "\n");
        }
    }
}
