import java.io.*;
import java.util.*;

/**
 * A helper will manually trace all bug and exception in the runtime.
 *
 */
public class Tracer 
{
	private static final String DEFAULT_LOG_FILE = "bug.log";
	private static String STRING_FORMAT = "UTF8";
	private static String logFilePath = DEFAULT_LOG_FILE;
	private static final String DEFAULT_MESSAGE_FORMAT = 
			"%1$s%1$s****************************************************%1$s[%2$s][%3$s]:%1$s%4$s";
	private static boolean isBusy = false;
	
	/**
	 * Set the path of the log file.
	 * @param fileName 
	 * 	Path to the log file. Example: "C:\bug.log",...
	 * @throws NullPointerException
	 */
	public static void setLogFile(String fileName) throws NullPointerException
	{
		logFilePath = fileName;
	}
	
	/**
	 * Write a message to the log file.
	 * @param threadName
	 * 	Name of the thread that called this method. 
	 * @param message
	 * 	The content stored in log file.
	 */
	public synchronized static void Log(String threadName, String message)
	{
		try
		{
			if(isBusy)
			{
				try
				{
					Tracer.class.wait();
				}
				catch (InterruptedException e) 
				{
					return;
				}
			}
			
			isBusy = true;
			
			//Get the current new line character of the system. 
			String strNewLine = System.getProperty("line.separator");
			
			//Get the current time.
			Calendar c = Calendar.getInstance();
			String strTimeNow = c.getTime().toString();
			
			//Create file stream writer objects to write the log contents.
			FileOutputStream outFile = new FileOutputStream(logFilePath, true);
			OutputStreamWriter streamWriter = new OutputStreamWriter(outFile, STRING_FORMAT);
			streamWriter.append(String.format(DEFAULT_MESSAGE_FORMAT, strNewLine, threadName, strTimeNow, message));
			
			//Flush and close the write stream.
			streamWriter.close();
		}
		catch (FileNotFoundException exc)
		{
			// TODO: handle exception
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		finally
		{
			//Release monitor.
			isBusy = false;
			Tracer.class.notify();
		}
	}
	
	/**
	 * Write a message to the log file.
	 * @param c
	 * 	The class that called this method. 
	 * @param message
	 * 	The content stored in log file.
	 */
	public synchronized static void Log(Class<?> c, String message)
	{
		Log(c.getName(), message);
	}
	
	/**
	 * Trace the information of an exception to the log file.
	 * @param threadName
	 * 	Name of the thread that called this method or the thread that raised the exception.
	 * @param exc
	 * 	The thrown exception needs tracing.
	 */
	public synchronized static void Log(String threadName, Exception exc)
	{
		Log(threadName, exc.toString());
	}	

	/**
	 * Trace the information of an exception to the log file.
	 * @param c
	 * 	The class that called this method. 
	 * @param exc
	 * 	The thrown exception needs tracing.
	 */
	public synchronized static void Log(Class<?> c, Exception exc)
	{
		Log(c.getName(), exc.toString());
	}
}
