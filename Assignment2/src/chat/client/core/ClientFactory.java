package chat.client.core;

import chat.client.network.ClientInterface;
import chat.client.network.ClientSocket;

public class ClientFactory
{
    private ClientInterface client;

    public ClientInterface getClient()
    {
        if (client == null) client = new ClientSocket();
        return client;
    }
}
