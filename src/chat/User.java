package chat;


import java.net.Socket;
import java.util.Date;

public class User {
    private String name;
    private String state;
    private Date date;
    private Socket socket;

    public User(String name, String state, Date date, Socket socket) {
        this.name = name;
        this.state = state;
        this.date = date;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public Date getDate() {
        return date;
    }
}
