package com.sc.model;

import java.time.LocalTime;

public class Clock extends Thread {
	private volatile int count = 0;
	private String id_Timer;
	private int startValue;
	private LocalTime creationTime;
	private LocalTime modifiedTime;
	private int stepTime;
	private Status status;

	public Clock(String id_Timer, int startValue, int stepTime, Status status) {
		super();
		this.modifiedTime = LocalTime.now();
		this.id_Timer = id_Timer;
		this.startValue = startValue;
		this.stepTime = stepTime;
		this.status = status;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getId_Timer() {
		return id_Timer;
	}

	public void setId_Timer(String id_Timer) {
		this.id_Timer = id_Timer;
	}

	public int getStartValue() {
		return startValue;
	}

	public void setstartValue(int startValue) {
		this.startValue = startValue;
	}

	public LocalTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public int getStepTime() {
		return stepTime;
	}

	public void setStepTime(int stepTime) {
		this.stepTime = stepTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalTime creationTime) {
		this.creationTime = creationTime;
	}

	public void run() {
		count = getStartValue();
		while (true) {
			try {
				System.out.println("Count value: " + count++ + " ******** " + getName());
				sleep(getStepTime() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public String toString() {
		return "Clock [count=" + count + ", id_Timer=" + id_Timer + ", startValue=" + startValue + ", modifiedTime="
				+ modifiedTime + ", stepTime=" + stepTime + ", status=" + status + "]";
	}
}
