package chat;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Server extends Thread{

    private Socket s;
    private User user;
    private volatile static List<User> userList = new ArrayList<User>();
    private static MessageKeeper messageKeeper = new MessageKeeper();
    ObjectInputStream oin;

    public static void main(String[] args) {
        try{
            int i = 0; // счетчик подключений
            ServerSocket server = new ServerSocket(1234);// слушаем порт 1234
            Socket socket;
            while(true) {

                socket = server.accept();
                if (getNumberOfUsers() < 2) {
                    String name;

                    System.out.println("Trying to connect");
//                if (getNumberOfUsers() >= 2){
//                    say(socket, "Sorry, too many users connected");
//                    socket.close();
//                    break;
//                }
                    while (true) {
                        name = (new BufferedReader(new InputStreamReader(socket.getInputStream()))).readLine();
                        name = name.substring(4); //todo fix
                        if (checkName(name)) {
                            say(socket, "OK");
                            break;
                        } else {
                            say(socket, "This name is already taken. Try another:");
                        }
                    }
//                if (getNumberOfUsers() >= 2){
//                    say(socket, "Sorry, too many users connected");
//                    socket.close();
//                    break;
//                }
                    i++;
                    new Server(getNewUser(name, socket), socket, name);
                    if (messageKeeper.getMessages() != null) {
                        say(socket, "------------------");
                        say(socket, "Previous comments:");
                        for (String s : messageKeeper.getMessages()) {
                            say(socket, s);
                        }
                        say(socket, "------------------");
                    }
                    System.out.println("New client: number " + i + ", Name: " + name);
                }else {
                    new CancelServer(socket);
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private Server(User user, Socket s, String name){
        this.user = user;
        this.s = s;

        try {
            addUser(s, name);
            oin = new ObjectInputStream(s.getInputStream());
        } catch (Exception e){
            e.printStackTrace();
        }

        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
            String input;
            Message message;
            while (/*(input = in.readLine()) != null  */ (message = (Message)(oin).readObject())!=null) {
//                Message msg = (Message)(oin.readObject());
                input = message.getMessage();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Client " + user.getName() + " disconnected");
                    delete(user);
                    break;
                }
                if (input.equalsIgnoreCase("number of users")) {
                    say(user.getSocket(), getNumberOfUsers() + "");
                } else {
                    messageKeeper.add(user.getName() + " : " + input);
                    say(input, user.getName());
//                System.out.println(input);
//                     oin = new ObjectInputStream(s.getInputStream());
                }
            }
//            closePrintWriters();
            in.close();
            s.close();//todo?
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static synchronized void say(String message, String name){
        for (User user: userList){    //todo
            try {
                Socket socket = user.getSocket();
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println(name + ": " + message);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static synchronized void say(Socket s, String message){       //do not use else!
        try {
            PrintWriter printWriter = new PrintWriter(s.getOutputStream(), true);
            printWriter.println(message);
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    private synchronized static void addUser(Socket socket, String name){
        userList.add(getNewUser(name, socket));
    }

    private static User getNewUser(String name, Socket socket) {
        return new User(name, "NaN", null, socket);
    }

    private synchronized void closePrintWriters(){
        for (Socket socket: getSockets()){
            try {
                socket.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static Socket[] getSockets(){
        Socket[] res = new Socket[userList.size()];
        for (int i = 0; i < userList.size(); i++){
            res[i] = userList.get(i).getSocket();
        }
        return res;
    }

    private void delete(User user){
        for (int i = 0; i < userList.size(); i++){
            if (userList.get(i).getName().equals(user.getName())){
                userList.remove(i);
            }
        }
    }
    private static int getNumberOfUsers(){
        return userList.size();
    }

    private static boolean checkName(String name){
        for (User user: userList){
            if (user.getName().equals(name)){
                return false;
            }
        }
        return true;
    }
}