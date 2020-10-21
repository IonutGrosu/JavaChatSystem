package chat.server.model;

import chat.shared.transferObjects.Message;
import chat.shared.transferObjects.User;
import chat.shared.util.Subject;

public interface ServerModelInterface extends Subject {
    void loginUser(User user);

    void sendActiveUsersToClient(String arg);

    void sendPublicMessage(Message arg);
}
