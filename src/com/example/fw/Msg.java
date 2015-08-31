package com.example.fw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Msg {
	private String text;
	
	public Msg(String text) {
		this.text = text;
	}
	
	public String getConfirmationLink(String text) {
		Pattern regex = Pattern.compile("http\\S*");
		Matcher matcher = regex.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}
	
}
