package com.ulricqin.frame.kit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {

	public static boolean isIdentifier(String name) {
		if (StringKit.isBlank(name)) {
			throw new IllegalArgumentException("argument is blank");
		}
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9\\-\\_]+");
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}
	
	public static boolean isDomain(String domain) {
		if (StringKit.isBlank(domain)) {
			throw new IllegalArgumentException("argument is blank");
		}
		
		Pattern pattern = Pattern.compile("^[a-z0-9\\-\\_][a-z0-9\\-\\_\\.]+[a-z]$");
		Matcher matcher = pattern.matcher(domain);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isDomain("_a.com"));
	}
}
