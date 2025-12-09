package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(8080);

        do {

            Socket s = ss.accept();
            ThreadHttp t = new ThreadHttp(s);
            t.start();
            
        } while (true);

    }
}