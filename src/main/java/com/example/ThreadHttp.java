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
            String metodo = parti[0];
            String path = parti[1];
            String protocollo = parti[2];

            do {

                response = in.readLine();
                System.out.println(response);

            } while (!response.isEmpty());

            String risposta = "";

            if(!richiestaValida(parti)){

                out.println("HTTP/1.1 400 AURA");
                out.println("");
                
            }
            else {

                switch(metodo){
                    case "GET":
                        if (path.endsWith("/")) {
                            path += "index.html";
                        }
        
                        File file = new File("Es" + path);
        
                        if (file.exists()) {
        
                            risposta = "HTTP/1.1 200 OK";
                            sendFile(file, outBinary, getType(path), risposta);
        
                        } else {

                            file = new File("Es/404.html");
                            risposta = "HTTP/1.1 404 NOT FOUND";
                            sendFile(file, outBinary, getType(path), risposta);
                
                        }
                        break;
                    case "POST":
                        break;
                    case "HEAD":
                        if (path.endsWith("/")) {
                            path += "index.html";
                        }
        
                        file = new File("Es" + path);
        
                        if (file.exists()) {
                            
                            risposta = "HTTP/1.1 200 OK";
                            out.println(risposta);
                            out.println("Content-Length: " + file.length());
                            out.println("Content-Type: " + getType(path));
                            out.println("");
        
                        } else {

                            File file2 = new File("Es/404.html");
                            risposta = "HTTP/1.1 404 NOT FOUND";
                            sendFile(file2, outBinary, getType(path), risposta);
                
                        }
                        break;
                    default:
                        risposta = "HTTP/1.1 405 METHOD NOT ALLOWED";
                        out.println(risposta);
                        out.println("");
                        break;
                }

            }

            s.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean richiestaValida(String[] parti) {
        if(parti == null || parti.length < 3) {
            return false;
        }
    
        if(parti[0].isEmpty()) {
            return false;
        }
    
        if(!parti[2].equals("HTTP/1.1") && !parti[2].equals("HTTP/1.0")) {
            return false;
        }
    
        return true;
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

    private static void sendFile(File file, DataOutputStream out, String contentType, String risposta) throws IOException {
        out.writeBytes(risposta + "\n");
        out.writeBytes("Content-Length: " + file.length() + "\n");
        out.writeBytes("Content-Type: " + contentType + "\n");
        out.writeBytes("\n");
    
        try (InputStream input = new FileInputStream(file)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = input.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
        }
    }

    private static String readBody(BufferedReader in, int contentLength) throws IOException {
        if (contentLength <= 0) {
            return "";
        }
        char[] buf = new char[contentLength];
        int read = 0;
        while (read < contentLength) {
            int n = in.read(buf, read, contentLength - read);
            if (n == -1) {
                break;
            }
            read += n;
        }
        return new String(buf, 0, read);
    }

}
