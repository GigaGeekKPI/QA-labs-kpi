import interfaces.IExceptionChecker;

public class ExceptionCheckerFactory {

    static IExceptionChecker exceptionChecker = null;

    static IExceptionChecker CreateExceptionChecker(){
        if (exceptionChecker != null)
            return exceptionChecker;
        else
            return new ExceptionChecker();
    }

    public static void setExceptionChecker(IExceptionChecker exceptionChecker) {
        ExceptionCheckerFactory.exceptionChecker = exceptionChecker;
    }
}
