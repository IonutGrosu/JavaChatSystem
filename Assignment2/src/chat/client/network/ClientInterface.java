package chat.client.network;

import chat.shared.util.Subject;

public interface ClientInterface extends Subject {
    void startClient(String username);

    void sendMessageToAll(String message);
}
