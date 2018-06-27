package com.ozz.tools.threadinfo;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ThreadInfo {
	public static void main(String[] args) throws UnknownHostException {
		String hostname = InetAddress.getLocalHost().getHostName();
		System.out.println("hostname: " + hostname);

		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		String pid = rt.getName();
		System.out.println("pid: " + pid);
		
		Thread t = Thread.currentThread();
		System.out.println("thread: " + t.getId() + "," + t.getName());
	}
}
