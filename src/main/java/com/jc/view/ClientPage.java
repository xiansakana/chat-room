package com.jc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;




public class ClientPage extends JFrame {

    private JTextArea ta = new JTextArea(10, 20);

    private JTextField tf = new JTextField(20);


    private static final String CONNSTR = "127.0.0.1";

    private static final int CONNPORT = 8080;
    private Socket socket = null;

    private DataOutputStream dataOutputStream = null;


    private boolean isConn = false;

    public ClientPage() throws HeadlessException {
        super();
    }

    public void init(String username) {
        this.setTitle(username);

        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(tf, BorderLayout.SOUTH);

        this.setBounds(300, 300, 400, 400);


        tf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String strSend = tf.getText();
                if (strSend.trim().length() == 0) {
                    return;
                }
                send(strSend, username);
                tf.setText("");


            }
        });


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ta.setEditable(false);
        tf.requestFocus();

        try {
            socket = new Socket(CONNSTR, CONNPORT);
            isConn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Receive()).start();

        this.setVisible(true);
    }

    public void send(String str, String username) {
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(username + " said: " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class Receive implements Runnable {
        @Override
        public void run() {
            try {
                while (isConn) {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String str = dataInputStream.readUTF();
                    SwingUtilities.invokeLater(() -> ta.append(str));
                    //ta.append(str);
                }
            } catch (SocketException e) {
                System.out.println("The server has stopped unexpectedly!");
                ta.append("The server has stopped unexpectedly!");
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}