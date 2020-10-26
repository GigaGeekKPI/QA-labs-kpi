import interfaces.IServer;

public class ServerFactory {

    static IServer server = null;

    static IServer CreateServer(){
        if (server != null)
            return server;
        else
            return new Server();
    }

    public static void setServer(IServer server) {
        ServerFactory.server = server;
    }
}
