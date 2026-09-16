import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    public LoginFrame() {

        setTitle("User Login");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel headingLabel = new JLabel("Login");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headingLabel.setBounds(165, 30, 120, 40);
        mainPanel.add(headingLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setBounds(50, 100, 100, 25);
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBounds(50, 130, 310, 40);
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setBounds(50, 185, 100, 25);
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(50, 215, 310, 40);
        mainPanel.add(passwordField);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBounds(50, 265, 150, 25);
        showPasswordCheckBox.setBackground(new Color(245, 245, 245));
        mainPanel.add(showPasswordCheckBox);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBounds(50, 305, 145, 42);
        mainPanel.add(loginButton);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.setBounds(215, 305, 145, 42);
        mainPanel.add(signupButton);

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        loginButton.addActionListener(e -> loginUser());

        signupButton.addActionListener(e -> {
            new SignupFrame();
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private void loginUser() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean loginSuccessful =
                UserDatabase.loginUser(username, password);

        if (loginSuccessful) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!\nWelcome, " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            showDashboard(username);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showDashboard(String username) {

        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setSize(500, 350);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 240, 255));

        JLabel welcomeLabel =
                new JLabel("Welcome to Dashboard, " + username + "!");

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBounds(65, 70, 400, 40);
        panel.add(welcomeLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setBounds(180, 170, 130, 45);
        panel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    dashboardFrame,
                    "Logout successful.",
                    "Logout",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dashboardFrame.dispose();
            new LoginFrame();
        });

        dashboardFrame.add(panel);
        dashboardFrame.setVisible(true);

        dispose();
    }
}