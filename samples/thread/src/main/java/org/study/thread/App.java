package org.study.thread;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class App
{
    public static void main( String[] args ) throws InterruptedException
    {
    	CMyCallableObject callObj = new CMyCallableObject("Study Thread Library", 10);
    	FutureTask<String> ftObj = new FutureTask<String>(callObj);
    	Thread runner = new Thread(ftObj);
    	runner.start();

    	try {
    		String result = ftObj.get(2, TimeUnit.SECONDS);
    		System.out.println("Future Task result: " + result + " In 2 seconds");
    	} catch (TimeoutException e) {
    		System.out.println("To cancel Future Task");
    		ftObj.cancel(true);
    		System.out.println("Future Task Runner's state: " + runner.getState());
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}
    }
}
