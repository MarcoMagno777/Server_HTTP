package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadHttp extends Thread {

    private Socket s;
    private PrintWriter out;
    private BufferedReader in;
    private DataOutputStream outBinary;

    ThreadHttp(Socket s) throws IOException {
        this.s = s;
        out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        outBinary = new DataOutputStream(s.getOutputStream());
    }

    @Override
    public void run() {

        try {

            String response = in.readLine();
            System.out.println(response);
            String[] parti = response.split(" ", 3);

            do {

                response = in.readLine();
                System.out.println(response);

            } while (!response.isEmpty());

            String risposta = "";

            if (parti[0].equals("GET")) {

                if (parti[1].endsWith("/")) {
                    parti[1] += "index.html";
                }

                File file = new File("Es" + parti[1]);
                System.out.println(file.getAbsolutePath());

                if (file.exists()) {

                    risposta = "HTTP/1.1 200 OK";

                    out.println(risposta);
                    out.println("Content-Length: " + file.length());
                    out.println("Content-Type: " + getType(parti[1]));
                    out.println("");

                    InputStream input = new FileInputStream(file);
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = input.read(buf)) != -1) {
                        outBinary.write(buf, 0, n);
                    }
                    input.close();

                } else {
                    risposta = "HTTP/1.1 404 NOT FOUND";
                    out.println(risposta);
                    out.println("");
                }

            } else {
                risposta = "HTTP/1.1 405 METHOD NOT ALLOWED";
                out.println(risposta);
                out.println("");
            }

            s.close();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String getType(String s) {
        String[] formato = s.split("\\.", 2);
        if (formato.length < 2) return "";  
        
        switch (formato[1].toLowerCase()) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";  
        }
    }

}
