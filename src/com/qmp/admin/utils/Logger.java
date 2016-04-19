package com.qmp.admin.utils;

import java.util.Date;

public class Logger {
	
	public static void log(String str){
		System.out.println("[" + new Date(System.currentTimeMillis()).toString() + "] " + str);
	}
	
	public static void error(String str){
		System.err.println("ERROR - [" + new Date(System.currentTimeMillis()).toString() + "] " + str);
	}

}
