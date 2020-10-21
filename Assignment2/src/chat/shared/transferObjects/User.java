package chat.shared.transferObjects;

import java.io.Serializable;

public class User  implements Serializable {
    String username;
    String avatarPath;

    public User()
    {

    }

    public User(String username, String avatarPath) {
        this.username = username;
        this.avatarPath = avatarPath;
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
