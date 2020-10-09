package chat.client.network;

import chat.shared.util.Subject;

public interface ClientInterface extends Subject {
    void startClient();

    void setUsername(String username);

    void sendMessageToAll(String message);
}
