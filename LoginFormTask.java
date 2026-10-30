import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

class WelcomePage extends Frame {
    public WelcomePage(User user) {
        super("Welcome");
        setLayout(new GridLayout(2, 1, 10, 10));
        add(new Label("Hello, " + user.getUsername(), Label.CENTER));
        add(new Label("Role: " + user.getRole(), Label.CENTER));
        setSize(320, 160);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

public class LoginFormTask extends Frame {
    private List<User> users = new ArrayList<>();
    private int wrongPasswordAttempts = 0;

    private TextField usernameField = new TextField(18);
    private TextField passwordField = new TextField(18);
    private Choice roleChoice = new Choice();
    private Button loginButton = new Button("Login");

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());

        Panel content = new Panel(new BorderLayout(10, 15));
        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        form.add(usernameField);

        form.add(new Label("Password:", Label.RIGHT));
        passwordField.setEchoChar('*');
        form.add(passwordField);

        form.add(new Label("Role:", Label.RIGHT));
        roleChoice.add("Student");
        roleChoice.add("TA");
        roleChoice.add("Professor");
        form.add(roleChoice);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancelButton = new Button("Cancel");
        buttons.add(cancelButton);
        buttons.add(loginButton);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        loginButton.addActionListener(e -> login());
        cancelButton.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password.");
            return;
        }

        User user = findUser(username);
        if (user == null) {
            showMessage("Username or password is incorrect.");
            return;
        }

        if (!user.getPassword().equals(password)) {
            wrongPasswordAttempts++;
            if (wrongPasswordAttempts >= 3) {
                loginButton.setEnabled(false);
                showMessage("Wrong password 3 times. Login is now disabled.");
            } else {
                showMessage("Incorrect password. Attempts remaining: "
                        + (3 - wrongPasswordAttempts));
            }
            return;
        }

        if (!user.getRole().equals(roleChoice.getSelectedItem())) {
            showMessage("Please select the correct role.");
            return;
        }

        new WelcomePage(user).setVisible(true);
        dispose();
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Login message", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(new Label(message, Label.CENTER), BorderLayout.CENTER);

        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        Panel panel = new Panel();
        panel.add(okButton);
        dialog.add(panel, BorderLayout.SOUTH);

        dialog.setSize(340, 130);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadUsers() {
        users.add(new User("student1", "pass123", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new LoginFormTask().setVisible(true));
    }
}
