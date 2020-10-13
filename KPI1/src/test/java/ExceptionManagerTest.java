import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

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
}