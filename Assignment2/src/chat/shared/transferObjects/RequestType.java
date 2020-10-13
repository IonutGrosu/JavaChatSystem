package chat.shared.transferObjects;

public enum RequestType {
    NEW_USER,
    DISCONNECT,
    CLOSE_SOCKET,
    UPDATE_ACTIVE_USERS,
    SEND_PRIVATE,
    SEND_PUBLIC,
    RECEIVE_PRIVATE,
    RECEIVE_PUBLIC
}
