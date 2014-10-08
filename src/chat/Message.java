package chat;

import java.io.Serializable;

public class Message implements Serializable{
    private String username;
    private String message;
    private String userState;

    public Message(String username, String message, String userState) {
        this.username = username;
        this.message = message;
        this.userState = userState;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getUserState() {
        return userState;
    }
}
