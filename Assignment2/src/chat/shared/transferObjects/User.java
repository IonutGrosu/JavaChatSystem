package chat.shared.transferObjects;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String avatarPath;

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
