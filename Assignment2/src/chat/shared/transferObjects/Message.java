package chat.shared.transferObjects;

import java.io.Serializable;

public class Message implements Serializable {
    private String messageBody;
    private String sender;

    public Message(String messageBody, String user)
    {
        this.messageBody = messageBody;
        this.sender = user;
    }

    public String getMessageBody()
    {
        return messageBody;
    }

    public String getSender()
    {
        return sender;
    }

    @Override public String toString()
    {
        return sender + ": " + messageBody;
    }
}
