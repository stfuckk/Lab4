package example.lab.Client;

public class ClientFactory
{
    public IClient createInstance()
    {
        return new Client();
    }
}
