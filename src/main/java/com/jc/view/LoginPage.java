package com.jc.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jc.async.AsyncCallback;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


public class LoginPage extends JFrame {
    private JPanel contentPane;
    public JTextField usernameField;
    private JTextField passwordField;
    private RegisterPage registerPage;

    public LoginPage() {
        registerPage = new RegisterPage();
        registerPage.setLoginPage(this);
        registerPage.setVisible(false);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 321);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel loginPageLabel = new JLabel("Login");
        loginPageLabel.setFont(new Font("Arial", Font.PLAIN, 30));

        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        usernameField = new JTextField();
        usernameField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setColumns(10);

        JButton loginBtn = getjButton();

        JButton registerBtn = new JButton("Register");
        registerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                registerPage.setVisible(true);
                setVisible(false);
            }
        });
        registerBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(loginPageLabel, GroupLayout.PREFERRED_SIZE, 426, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(24)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18)
                                                                .addComponent(passwordField))
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(44)
                                                .addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                                .addGap(61)
                                                .addComponent(registerBtn, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(loginPageLabel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                .addGap(44)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(loginBtn)
                                        .addComponent(registerBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addGap(13))
        );
        contentPane.setLayout(gl_contentPane);
    }

    private JButton getjButton() {
        JButton loginBtn = new JButton("Login");
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{1,16}$");
                Pattern passwordPattern = Pattern.compile("^\\S{1,16}$");
                if ((passwordField.getText()).isEmpty() || (usernameField.getText()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username or Password cannot be empty!!!");
                } else if ((passwordField.getText()).length() > 16 || (usernameField.getText()).length() > 16) {
                    JOptionPane.showMessageDialog(null, "The length of Username or Password should be between 1 and " +
                            "16!!!");
                }else if (!usernamePattern.matcher(usernameField.getText()).matches() || !passwordPattern.matcher(passwordField.getText()).matches()) {
                    JOptionPane.showMessageDialog(null, "Username or Password is invalid.");
                } else {
                    loginUserAsync(usernameField.getText(), passwordField.getText(), new AsyncCallback() {
                        @Override
                        public void onResponse(String response) {

                            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                            String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown Error";
                            SwingUtilities.invokeLater(() -> {

                                if(!message.equals("success")){
                                    JOptionPane.showMessageDialog(null, message);
                                }else{
                                    JOptionPane.showMessageDialog(null," Account login successfully!");
                                    setVisible(false);
                                    ClientPage clientPage = new ClientPage();
                                    clientPage.init(usernameField.getText());

                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, e.getMessage());
                            });
                        }
                    });
                }
            }
        });
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 16));//////
        return loginBtn;
    }

    public void loginUserAsync(String username, String password, AsyncCallback callback) {
        String form = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8088/user/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    if(callback != null) {
                        callback.onResponse(responseBody);
                    }
                })
                .exceptionally(e -> {
                    if(callback != null) {
                        callback.onFailure((Exception) e);
                    }
                    return null;
                });
    }

}