package Message;

/*
*******************
CHAT MESSAGE FORMAT
*******************

String channel, String sender, String login, String hostname, String message

*/

public class Message_Chat extends Message {
    public Message_Chat(String... A) {
        super(A);
        this.type = Message_Type.MSG_CHAT;
    }
    
    public String getChannel() {
        return content.get(0);
    }
    
    public String getSender() {
        return content.get(1);
    }
    
    public String getText() {
        return content.get(4);
    }
    
    public String getLogin() {
        return content.get(2);
    }
    
    public String getHostname() {
        return content.get(3);
    }

    public boolean isValid() {
        return content.size() == 5;
    }
}
