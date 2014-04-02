package com.superSmily.timeCounter;

public class Activity {
	String id;
	String name;
	long baseChrono;
	long timeRunning;
	boolean isRunning;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getBaseChrono() {
		return baseChrono;
	}
	public void setBaseChrono(long baseChrono) {
		this.baseChrono = baseChrono;
	}
	public long getTimeRunning() {
		return timeRunning;
	}
	public void setTimeRunning(long timeRunning) {
		this.timeRunning = timeRunning;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
