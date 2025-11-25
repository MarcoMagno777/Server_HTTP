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

        do {

            String response = in.readLine();

            System.out.println(response);

            String[] parti = response.split(" ", 3);

            do {

                response = in.readLine();
                System.out.println(response);
            
            } while (!response.isEmpty());

            System.out.println("Fine richiesta. Inizio risposta");

            String header = "";
            String campiHeader = "";
            String data = "";

            switch (parti[1]) {
                case "/ciao.html":
                    header = "HTTP/1.1 200 OK";
                    data = "<h1>ciao</h1>";
                    break;
                case "/ciao":
                    header = "HTTP/1.1 301 MOVED PERMANENTLY";
                    campiHeader = "Location: /ciao.html";
                    break;
                default:
                    header = "HTTP/1.1 404 NOT FOUND";
                    break;
            }

            out.println(header);
            out.println("Content-Type: text/html");
            out.println("Content-Length: " + data.length());
            if(!campiHeader.isEmpty()){
                out.println(campiHeader);
            }
            out.println("");
            out.println(data);
            
        }while(s.isConnected());

        

    }
}