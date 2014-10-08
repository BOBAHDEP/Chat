package chat;


import java.io.PrintWriter;
import java.net.Socket;

public class CancelServer  extends Thread{
    private Socket s;

    public CancelServer(Socket s) {
        this.s = s;

        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    @Override
    public void run(){
        try{
            say(s,"");
            say(s, "Sorry, there are too many users.");
            say(s, "Server message: too many users");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static synchronized void say(Socket s, String message){
        try {
            PrintWriter printWriter = new PrintWriter(s.getOutputStream(), true);
            printWriter.println(message);
        } catch (Exception e){
            e.printStackTrace();

        }
    }
}
