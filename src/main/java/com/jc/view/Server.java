package com.jc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Server extends JFrame{
    JTextArea serverTa = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(serverTa);
    JPanel btnTool = new JPanel();
    JButton startBtn = new JButton("Start");
    JButton stopBtn = new JButton("Stop");
    private static final int PORT = 8080;
    //ServerSocket
    private ServerSocket serverSocket = null;
    private Socket socket = null;


    private ArrayList<ClientCoon> ccList = new ArrayList<ClientCoon>();

    private boolean isStart = false;

    public void init() throws Exception {
        this.setTitle("Server");
        this.add(scrollPane, BorderLayout.CENTER);
        serverTa.setEditable(false);
        btnTool.add(startBtn);
        btnTool.add(stopBtn);
        this.add(btnTool, BorderLayout.SOUTH);
        this.setBounds(0, 0, 500, 500);

        if (isStart) {
            System.out.println("Server already start\n");
        } else {
            System.out.println("Server not start yet, click to start\n");
        }

        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (serverSocket == null) {
                        serverSocket = new ServerSocket(PORT);
                    }
                    isStart = true;
                    serverTa.append("Server started! \n");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                        isStart = false;
                    }
                    System.exit(0);
                    serverTa.append("Server disconnected! \n");
                    System.out.println("Server disconnected! \n");

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        this.setVisible(true);
        startServer();
    }


    public void startServer() throws Exception {
        try {
            try {
                serverSocket = new ServerSocket(PORT);
                isStart = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (isStart) {
                socket = serverSocket.accept();
                ccList.add(new ClientCoon(socket));
                System.out.println("\n" + "One client connected to the server" + socket.getInetAddress() + "/" + socket.getPort());
                serverTa.append("\n" + "One client connected to the server" + socket.getInetAddress() + "/" + socket.getPort());
            }
        } catch (SocketException e) {
            System.out.println("Server disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientCoon implements Runnable {
        Socket socket = null;

        public ClientCoon(Socket socket) {
            this.socket = socket;

            (new Thread(this)).start();
        }


        @Override
        public void run() {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                while (isStart) {
                    String str = dataInputStream.readUTF();
                    System.out.println(str + "\n");
                    serverTa.append(str + "\n");
                    String strSend = str + "\n";
                    for (ClientCoon clientCoon : ccList) {
                        clientCoon.send(strSend);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void send(String str) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
                dataOutputStream.writeUTF(str);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
