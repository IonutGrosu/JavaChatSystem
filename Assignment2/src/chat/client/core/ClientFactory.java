package chat.client.core;

import chat.client.network.ClientInterface;
import chat.client.network.Client;

public class ClientFactory
{
    private ClientInterface client;

    public ClientInterface getClient()
    {
        if (client == null) client = new Client();
        return client;
    }
}
