package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(3000);
        Socket s = ss.accept();

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        String response = in.readLine();

        System.out.println(response);

        do {

            response = in.readLine();
            System.out.println(response);
            
        } while (!response.isEmpty());

        System.out.println("Fine richiesta. Inizio risposta");

        String data = "<h2>Giulia lunghi</h2>";

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("Content-length: " + data.length());
        out.println("");
        out.println(data);

    }
}