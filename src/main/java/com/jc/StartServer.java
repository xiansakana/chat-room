package com.jc;


import com.jc.view.Server;

public class StartServer {

    public static void main(String[] args) {

        new Thread(() -> {
            Server server = new Server();
            try {
                server.init();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
