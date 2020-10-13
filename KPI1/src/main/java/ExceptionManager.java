import java.util.ArrayList;
import java.util.List;

public class ExceptionManager {

    private List<Class<? extends Exception>> CriticalExceptions = new ArrayList<Class<? extends Exception>>();
    private int CriticalExceptionCount = 0;
    private int NonCriticalExceptionCount = 0;

    public int getCriticalExceptionCount() {
        return CriticalExceptionCount;
    }

    public int getNonCriticalExceptionCount() {
        return NonCriticalExceptionCount;
    }

    void AddCriticalException(Class<? extends Exception> ExceptionClass)
    {
        CriticalExceptions.add(ExceptionClass);
    }

    boolean IsExceptionCritical(Exception ex)
    {
        if (ex != null) {
            for (Class<? extends Exception> ExClass : CriticalExceptions) {
                if (ExClass.isInstance(ex))
                    return true;
            }
        }
        return false;
    }

    void ProcessException(Exception ex)
    {
        if (IsExceptionCritical(ex))
            CriticalExceptionCount++;
        else
            NonCriticalExceptionCount++;
    }
}
