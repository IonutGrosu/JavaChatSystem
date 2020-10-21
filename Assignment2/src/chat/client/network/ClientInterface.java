package chat.client.network;

import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.User;
import chat.shared.util.Subject;

public interface ClientInterface extends Subject {
    void startClient();

    void login(User user);

    void getActiveUsersList(User user);

    void sendPublicMessage(Message messageToSend);

    void disconnect(User user);
}
