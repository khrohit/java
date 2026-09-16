import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;

    public SignupFrame() {

        setTitle("Create Account");
        setSize(430, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel headingLabel = new JLabel("Create Account");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headingLabel.setBounds(105, 25, 250, 40);
        mainPanel.add(headingLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setBounds(55, 90, 120, 25);
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBounds(55, 120, 310, 40);
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setBounds(55, 175, 120, 25);
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(55, 205, 310, 40);
        mainPanel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmPasswordLabel.setBounds(55, 260, 170, 25);
        mainPanel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmPasswordField.setBounds(55, 290, 310, 40);
        mainPanel.add(confirmPasswordField);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBounds(55, 340, 160, 25);
        showPasswordCheckBox.setBackground(new Color(245, 245, 245));
        mainPanel.add(showPasswordCheckBox);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAccountButton.setBounds(55, 385, 150, 45);
        mainPanel.add(createAccountButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(215, 385, 150, 45);
        mainPanel.add(backButton);

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
                confirmPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
                confirmPasswordField.setEchoChar('•');
            }
        });

        createAccountButton.addActionListener(e -> createAccount());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private void createAccount() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username must contain at least 3 characters.",
                    "Invalid Username",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 4 characters.",
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password and confirm password do not match.",
                    "Password Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean registrationSuccessful =
                UserDatabase.registerUser(username, password);

        if (registrationSuccessful) {
            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!\nYou can now login.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new LoginFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Username already exists.\nPlease choose another username.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}