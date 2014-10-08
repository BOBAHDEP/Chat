package chat;


public class MessageKeeper {
    private String[] messages = new String[10];
    private int length = 0;

    public void add(String message){
        if (length < 10){
            messages[length] = message;
            length++;
        } else {
            for (int i = 0; i < 9; i++){
                messages[i] = messages[i+1];
                messages[9] = message;
            }
        }
    }

    public String[] getMessages(){
        if (length == 0){
            return null;
        }
        String[] res = new String[length];
        for (int i = 0; i < length; i++){
            res[i] = messages[i];
        }
        return res;
    }
}
