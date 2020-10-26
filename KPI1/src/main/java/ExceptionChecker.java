import exceptions.ExceptionA;
import interfaces.IExceptionChecker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExceptionChecker implements IExceptionChecker {

    private List<Class<? extends Exception>> CriticalExceptions = new ArrayList<>();

    @Override
    public boolean IsExceptionCritical(Exception exception) {
        if (exception != null) {
            for (Class<? extends Exception> ExClass : CriticalExceptions) {
                if (ExClass.isInstance(exception))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void AddCriticalException(Class<? extends Exception> ExceptionClass) {
        CriticalExceptions.add(ExceptionClass);
    }

    @Override
    public void LoadCriticalExceptionsListFromFile(String fileName) {
        Properties properties = new Properties();

        try {
            InputStream inputStream = new FileInputStream(fileName);
            properties.load(inputStream);
        }
        catch (java.lang.Exception exception)
        {
            exception.printStackTrace();
        }

        String criticalExceptionString = properties.getProperty("criticalExceptions");

        for (String criticalExceptionName : criticalExceptionString.split(",")) {
            Class<? extends Exception> exceptionClass;
            try {
                exceptionClass = (Class<? extends Exception>) Class.forName("exceptions." + criticalExceptionName);
            }
            catch (java.lang.Exception exception){
                exceptionClass = null;
            }

            if (exceptionClass != null) {
                CriticalExceptions.add(exceptionClass);
            }
        }
    }
}
