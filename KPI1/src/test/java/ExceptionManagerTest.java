import exceptions.ExceptionA;
import exceptions.ExceptionB;
import exceptions.ExceptionC;
import interfaces.IExceptionChecker;
import interfaces.IServer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class ExceptionManagerTest {

    @org.junit.Test
    @Parameters({"true", "false"})
    public void isExceptionCriticalTest(boolean CheckCriticalException) {
        ExceptionManager ExManager = new ExceptionManager();
        Exception ex = new ExceptionA();

        if (CheckCriticalException)
            ExManager.AddCriticalException(ExceptionA.class);

        assertEquals(CheckCriticalException, ExManager.IsExceptionCritical(ex));
    }

    @org.junit.Test
    @Parameters({"true", "false"})
    public void checkIsExceptionCriticalTest (boolean CheckCriticalException) {
        IExceptionChecker exceptionChecker = mock(ExceptionChecker.class);
        ExceptionManager ExManager = new ExceptionManager(exceptionChecker, ServerFactory.CreateServer());
        when(exceptionChecker.IsExceptionCritical(any(ExceptionA.class))).thenReturn(CheckCriticalException);
        Exception ex = new ExceptionA();

        if (CheckCriticalException)
            ExManager.AddCriticalException(ExceptionA.class);

        assertEquals(CheckCriticalException, ExManager.IsExceptionCritical(ex));
    }

    @org.junit.Test
    public void isDerivedExceptionCriticalTest() {
        ExceptionManager ExManager = new ExceptionManager();
        Exception ex = new ExceptionB();

        ExManager.AddCriticalException(ExceptionA.class);

        assertTrue(ExManager.IsExceptionCritical(ex));
    }

    @org.junit.Test
    @Parameters({"4, 5", "2, 1", "3, 6"})
    public void processExceptionTest(int CriticalExceptionsCount, int NonCriticalExceptionsCount) {
        ExceptionManager ExManager = new ExceptionManager();
        ExManager.AddCriticalException(ExceptionA.class);

        for (int i = 0; i < CriticalExceptionsCount; i++)
        {
            Exception ex = new ExceptionA();
            ExManager.ProcessException(ex);
        }

        for (int i = 0; i < NonCriticalExceptionsCount; i++)
        {
            Exception ex = new ExceptionC();
            ExManager.ProcessException(ex);
        }

        assertEquals(CriticalExceptionsCount, ExManager.getCriticalExceptionCount());
        assertEquals(NonCriticalExceptionsCount, ExManager.getNonCriticalExceptionCount());
    }

    @org.junit.Test
    @Parameters({"1, true", "3, true", "9, false"})
    public void SeverResponsesTest (int numberOfExceptions, boolean expectedResponse) {
        IServer server = mock(Server.class);
        ExceptionManager exceptionManager = new ExceptionManager(ExceptionCheckerFactory.CreateExceptionChecker(), server);
        exceptionManager.AddCriticalException(ExceptionA.class);
        when(server.SendExceptionToServer(any())).thenReturn(expectedResponse);

        for  (int i = 0; i < numberOfExceptions; i++) {
            exceptionManager.ProcessException(new ExceptionA());
        }

        assertEquals(expectedResponse ? 0 : numberOfExceptions, exceptionManager.getFailedRequestsCount());
    }

    @org.junit.Test
    public void LoadCriticalExceptionsListFromFileTest () {
        ExceptionManager exceptionManager = new ExceptionManager();
        exceptionManager.LoadCriticalExceptionsListFromFile("src/main/resources/application.properties");

        assertTrue(exceptionManager.IsExceptionCritical(new ExceptionA()));
        assertTrue(exceptionManager.IsExceptionCritical(new ExceptionB()));
    }
}