package org.study.thread;

import java.util.concurrent.Callable;

public class CMyCallableObject implements Callable<String>  {
	private String m_Name;
	private int m_Worktime;

	public CMyCallableObject(String name, int seconds) {
		m_Name = name;
		m_Worktime = seconds;
	}

	public String call() throws Exception {
		System.out.println("CMyCallableObject call <IN>");
		Thread.sleep(m_Worktime * 1000);
		System.out.println("CMyCallableObject call <OUT>");
		return m_Name + m_Worktime;
	}
}
