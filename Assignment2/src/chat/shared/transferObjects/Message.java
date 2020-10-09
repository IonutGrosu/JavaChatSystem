package chat.shared.transferObjects;

import java.io.Serializable;

public class Message implements Serializable {
    private String messageBody;
    private String user;

    public Message(String messageBody, String user)
    {
        this.messageBody = messageBody;
        this.user = user;
    }

    public String getMessageBody()
    {
        return messageBody;
    }

    public String getUser()
    {
        return user;
    }

    @Override public String toString()
    {
        return user + ": " + messageBody;
    }
}
