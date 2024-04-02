package com.jc.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jc.async.AsyncCallback;
import lombok.Setter;
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

public class RegisterPage extends JFrame {
    private JPanel contentPane;
    private JTextField usernameField;
    private JTextField passwordField;
    @Setter
    private LoginPage loginPage;

    public RegisterPage() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel createAccountLabel = new JLabel("Register");
        createAccountLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        usernameField = new JTextField();
        usernameField.setColumns(10);

        passwordField = new JTextField();
        passwordField.setColumns(10);

        JButton submitBTn = getjButton();

        JButton backBtn = new JButton("backToLogin");
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                loginPage.setVisible(true);
            }
        });
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(createAccountLabel, GroupLayout.PREFERRED_SIZE, 426, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(32)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                                .addComponent(passwordLabel)
                                                                .addGap(18)
                                                                .addComponent(passwordField)))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.LEADING, gl_contentPane.createSequentialGroup()
                                .addGap(45)
                                .addComponent(backBtn, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                                .addComponent(submitBTn, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                .addGap(55))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(createAccountLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                                .addGap(29)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(backBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(submitBTn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                                .addGap(24))
        );
        contentPane.setLayout(gl_contentPane);
    }

    private JButton getjButton() {
        JButton submitBTn = new JButton("Submit");

        submitBTn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{1,16}$");
                Pattern passwordPattern = Pattern.compile("^\\S{1,16}$");
                if((passwordField.getText()).isEmpty() || (usernameField.getText()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username or Password cannot be empty!!!");
                }else if((passwordField.getText()).length() > 16 || (usernameField.getText()).length() > 16){
                    JOptionPane.showMessageDialog(null, "The length of Username or Password should be between 1 and " +
                            "16!!!");
                }else if (!usernamePattern.matcher(usernameField.getText()).matches() || !passwordPattern.matcher(passwordField.getText()).matches()) {
                    JOptionPane.showMessageDialog(null, "Username or Password is invalid.");
                } else{
                        RegisterUserAsync(usernameField.getText(), passwordField.getText(),
                                new AsyncCallback() {
                                    @Override
                                    public void onResponse(String response) {
                                        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                                        String message = jsonResponse.has("message") ?
                                                jsonResponse.get("message").getAsString() : "Unknown Error";
                                        SwingUtilities.invokeLater(() -> {
                                            if(!message.equals("success")){
                                                JOptionPane.showMessageDialog(null, message);
                                            }else{
                                                JOptionPane.showMessageDialog(null,"Account registered successfully!");
                                                loginPage.setVisible(true);
                                                setVisible(false);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        // 处理异常情况
                                        SwingUtilities.invokeLater(() -> {
                                            JOptionPane.showMessageDialog(null, "Registration failed: " + e.getMessage());
                                        });
                                    }
                                });

                }
            }
        });
        return submitBTn;
    }

    public void RegisterUserAsync(String username, String password, AsyncCallback callback) {
        String form = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8088/user/register"))
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

