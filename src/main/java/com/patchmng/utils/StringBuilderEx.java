package com.patchmng.utils;

public class StringBuilderEx {
	
	private StringBuilder sb;
	
	public StringBuilderEx(){
		this.sb = new StringBuilder();
	}
	
	public StringBuilderEx(String text){
		this.sb = new StringBuilder();
		this.sb.append(text);
	}	
	
	public StringBuilder getSb() {
		return sb;
	}
	
	public StringBuilderEx append(Object item){
		this.sb.append(item);
		return this;
	}	
	
	public StringBuilderEx appendRow(String item){
		this.sb.append(item);
		this.sb.append("\n");
		return this;
	}
	
	public StringBuilderEx appendFormater(String format, Object... args){
		this.sb.append(String.format(format, args));	
		return this;
	}
	
	public StringBuilderEx appendFormaterRow(String format, Object... args){
		this.sb.append(String.format(format, args));
		this.sb.append("\n");
		return this;
	}	
	
	public StringBuilderEx appendFR(String format, Object... args){
		return appendFormaterRow(format, args);
	}	
	
	public String toString(){
		return this.sb.toString();
	}
	
	public int length(){ 
		return this.sb.length();
	}

}
