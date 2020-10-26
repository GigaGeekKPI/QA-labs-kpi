import interfaces.IServer;

public class Server implements IServer {
    private float RequestSuccessChance = 0.9f;

    public Server(){

    }

    public Server(float requestSuccessChance) {
        RequestSuccessChance = requestSuccessChance;
    }

    @Override
    public boolean SendExceptionToServer(Exception ex) {
        return Math.random() <= RequestSuccessChance;
    }

    public void setRequestSuccessChance(float requestSuccessChance) {
        RequestSuccessChance = requestSuccessChance;
    }
}
