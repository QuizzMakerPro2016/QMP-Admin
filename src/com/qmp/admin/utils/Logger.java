package com.qmp.admin.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Logger {
	
	private static String date(){
		LocalDateTime date = LocalDateTime.now();
		String nano = String.valueOf(date.getNano());
		String text = date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + ":" + nano.substring(0, 4) ;
		return "[" + text + "] ";
	}
	
	public static void log(String str){
		System.out.println(date() + str);
	}
	
	public static void log(Object o){
		log(o.toString());
	}
	
	public static void error(String str){
		System.err.println(date() + "ERROR: " + str);
	}
	
	public static void error(Object o){
		error(o.toString());
	}
	
	public static void warn(String str){
		System.err.println(date() + "WARNING: " + str);
	}
	
	public static void warn(Object o){
		error(o.toString());
	}

}
