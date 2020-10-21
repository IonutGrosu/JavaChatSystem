package chat.shared.transferObjects;

import java.io.Serializable;

public class Message implements Serializable {
    private String messageBody;
    private User sender;

    public Message(String messageBody, User user)
    {
        this.messageBody = messageBody;
        this.sender = user;
    }

    public String getMessageBody()
    {
        return messageBody;
    }

    public String getSenderUsername()
    {
        return sender.getUsername();
    }

    public User getSender(){
        return sender;
    }

    @Override public String toString()
    {
        return sender.getUsername() + ": " + messageBody;
    }
}
