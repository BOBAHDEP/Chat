package chat;

import java.net.*;
import java.io.*;
import java.util.Scanner;

class Client extends Thread{

    public Socket clientSocket;
    public BufferedReader in;
    public PrintWriter out;
    public BufferedReader inu;
    public String fuser, fserver;
    private volatile boolean validate = false;
    private volatile boolean disconnected = false;

    private ObjectOutputStream oos;

    {
        try {
            clientSocket = new Socket("localhost", 1234); // 127.0.0.1 - IP где запущен Server, 1234 - порт
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            inu = new BufferedReader(new InputStreamReader(System.in));
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client client = new Client();

        try {
            client.setDaemon(true);
            client.setPriority(NORM_PRIORITY);
            client.start();
            System.out.print("Please enter your name:");
            client.out.println("");
            while (!client.validate ) {
                client.out.println(getUserName());
                Thread.sleep(1000);
            }
            client.oos = new ObjectOutputStream(client.clientSocket.getOutputStream());
            while (!client.disconnected) {

                if ((client.fuser = client.inu.readLine()) != null) {

                    Message message = new Message(client.getName(), client.fuser,"Ok");
                    client.oos.writeObject(message);
                    client.oos.flush();
                    if (client.fuser.equalsIgnoreCase("exit")) {
                        client.disconnected = true;
                        System.out.println("Disconnected");
                        break;
                    }
                }
            }
            client.out.close();
            client.in.close();
            client.inu.close();
            client.oos.close();
            client.clientSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        try {
            while (true) {
                if ((fserver = in.readLine()) != null) {
                    if (fserver.equals("Server message: too many users")) {
                        System.exit(0);
                        break;
                    }
                    if (!validate && fserver.equals("OK")){
                        System.out.println("You are connected.");
                        validate = true;
                    } else {
                        System.out.println(fserver);
                    }
                }
            }
        }catch (Exception e){
            if (e.getMessage().equals("socket closed")){
                System.out.println("You are out of server");
            }else {
                e.printStackTrace();
            }
        }
    }

    private static String getUserName(){

        Scanner in = new Scanner(System.in);
        String res = in.next();
        System.out.print("Please enter password:");
        return res+":"+in.next();
    }
}