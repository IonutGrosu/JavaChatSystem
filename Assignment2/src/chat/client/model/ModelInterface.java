package chat.client.model;

import chat.shared.util.Subject;

public interface ModelInterface extends Subject
{
    void login(String username);

    void getActiveUsersList();

    void sendPublicMessage(String value);

    void getUsername();

    void getFirstImages();

    void getNextAvatarImage(int currentIndex);

    void getPreviousAvatarImage(int currentIndex);

    void disconnect();
}
