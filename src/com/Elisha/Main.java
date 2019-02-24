package com.Elisha;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static final String path = System.getProperty("user.dir"); // her får man c:/user/Alvin/...

    public static void main(String[] args) {

        System.out.println(path);

        try {
            ServerSocket serverSocket = new ServerSocket(1337);

            while (true){

                System.out.println("Afventer forbindelse...");

                Socket socket = serverSocket.accept(); // den blokerer

                System.out.println("forbindelse oprettet");

                serverSocketTheClient(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void serverSocketTheClient(Socket socket) {

        try {
            Scanner fromClient = new Scanner(socket.getInputStream());
            System.out.println("fra Client");

            String fromClientString = fromClient.nextLine();
            System.out.println(fromClientString);

            StringTokenizer stringTokenizer = new StringTokenizer(fromClientString);
            System.out.println(fromClientString);

            System.out.println("token");
            System.out.println(stringTokenizer.nextToken());
            System.out.println("stop token");

            String tokenString = stringTokenizer.nextToken();
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String pathIndex = path + tokenString;
            File file = new File(pathIndex);
            SearchFile(dataOutputStream, file);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void SearchFile(DataOutputStream dataOutputStream, File file) throws IOException {

        if (!file.isFile()){
            System.out.println("filen ikke fundet");
        }else{
            // send svar til client(browser):
            System.out.println("filen fundet");

            FileFound(dataOutputStream, file);
        }
    }

    private static void FileFound(DataOutputStream dataOutputStream, File file) throws IOException {

        int lenght = (int) file.length();
        byte[] byteArr = new byte[lenght];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteArr);
        fileInputStream.close();

        dataOutputStream.writeBytes("/HTTP//1.0 200 Her kommer Data\r\n");
        dataOutputStream.writeBytes("Content-Lenght: " + lenght + "\r\n");
        dataOutputStream.writeBytes("\r\n"); // vigtig: denne adskiller header fra indhold

        dataOutputStream.write(byteArr, 0, lenght);
        dataOutputStream.writeBytes("\n"); // check om det er nødvendigt
    }
}
