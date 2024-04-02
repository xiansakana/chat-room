package com.jc;

import com.jc.view.LoginPage;

import java.awt.*;

public class StartClient {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginPage loginPage = new LoginPage();
                    loginPage.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}