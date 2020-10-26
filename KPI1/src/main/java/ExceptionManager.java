import interfaces.IExceptionChecker;
import interfaces.IServer;

import java.io.InputStream;
import java.util.Properties;

public class ExceptionManager {

    private IExceptionChecker exceptionChecker;
    private IServer server;

    private int CriticalExceptionCount = 0;
    private int NonCriticalExceptionCount = 0;
    private int FailedRequestsCount = 0;

    public ExceptionManager() {
        this.server = ServerFactory.CreateServer();
        this.exceptionChecker = ExceptionCheckerFactory.CreateExceptionChecker();
    }

    public ExceptionManager(IExceptionChecker exceptionChecker, IServer server) {
        this.exceptionChecker = exceptionChecker;
        this.server = server;
    }

    public int getCriticalExceptionCount() {
        return CriticalExceptionCount;
    }

    public int getNonCriticalExceptionCount() {
        return NonCriticalExceptionCount;
    }

    public int getFailedRequestsCount() {
        return FailedRequestsCount;
    }

    void AddCriticalException(Class<? extends Exception> ExceptionClass)
    {
        exceptionChecker.AddCriticalException(ExceptionClass);
    }

    boolean IsExceptionCritical(Exception ex)
    {
        return exceptionChecker.IsExceptionCritical(ex);
    }

    void ProcessException(Exception ex)
    {
        if (IsExceptionCritical(ex)) {
            CriticalExceptionCount++;

            if (!server.SendExceptionToServer(ex))
                FailedRequestsCount++;
        }
        else {
            NonCriticalExceptionCount++;
        }
    }

    public void LoadCriticalExceptionsListFromFile(String fileName) {
        exceptionChecker.LoadCriticalExceptionsListFromFile(fileName);
    }

    public void setExceptionChecker(IExceptionChecker exceptionChecker) {
        this.exceptionChecker = exceptionChecker;
    }

    public void setServer(IServer server) {
        this.server = server;
    }

    public void Reset(){
        CriticalExceptionCount = 0;
        NonCriticalExceptionCount = 0;
        FailedRequestsCount = 0;
    }
}
