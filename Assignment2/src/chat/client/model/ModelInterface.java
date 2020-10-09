package chat.client.model;

import chat.shared.util.Subject;

public interface ModelInterface extends Subject
{
    void setUsername(String username);

    void startClient();

    void sendMessage(String message);
}
