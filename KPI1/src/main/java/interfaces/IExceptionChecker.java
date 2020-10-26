package interfaces;

public interface IExceptionChecker {
    boolean IsExceptionCritical (Exception exception);
    void AddCriticalException (Class<? extends Exception> ExceptionClass);
    void LoadCriticalExceptionsListFromFile (String fileName);
}
