package chat.client.model;

import chat.shared.util.Subject;

public interface ModelInterface extends Subject
{
    void startClient(String username);

    void sendMessage(String message);
}
