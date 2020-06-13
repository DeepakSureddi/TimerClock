package com.sc.controller;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.model.Clock;
import com.sc.model.Status;

@RestController
@RequestMapping(produces="application/json")
public class TimerController extends Thread {

	static Timer timer;
	static String id = "0";
	ObjectMapper mapper = new ObjectMapper();
	Set<String> s = new HashSet<>();
	Map<String, Clock> clockTimers = new HashMap<String, Clock>();
	
	@RequestMapping("/create")
	public String showStartCheckin3(@RequestParam("startVal") Integer startVal,
			@RequestParam("stepTime") Integer stepTime) {
		Integer valueOf = Integer.valueOf(id) + 1;
		id = valueOf.toString();
		Clock clock = new Clock(id, startVal, stepTime, Status.RUNNING);
		clock.setName(id);
		clock.setCreationTime(LocalTime.now());
		clock.start();
		s.add(id);
		clockTimers.put(id, clock);
		return "created thread with id:" + id;
	}
	
	@RequestMapping("/check")
	public String checkWithId(@RequestParam("id") String id) throws JsonProcessingException {
		Clock clock = clockTimers.get(id);
		return "count: " + clock.getCount() + " \nCreation Time: " + clock.getCreationTime() + "\nStep Time: " + clock.getStepTime() + "\n\n";
	}

	@RequestMapping("/checkActiveTimers")
	public Set<String> check() {
		Collection<Clock> values = clockTimers.values();
		Set<String> s = new HashSet<>(); 
		for (Clock clock : values) {
			s.add("count: " + clock.getCount() + " \nCreation Time: " + clock.getCreationTime() + "\nStep Time: " + clock.getStepTime() + "\n\n");
		}
		return s;
	}

	@RequestMapping("/render")
	public Set<String> startCheckIn(HttpServletResponse httpServletResponse) {
		Collection<Clock> values = clockTimers.values();
		Set<String> s = new HashSet<>(); 
		for (Clock clock : values) {
			clock.setModifiedTime(LocalTime.now());
			s.add(clock.toString());
		}
		return s;
	}

	@RequestMapping("/clear")
	public String clearId(@RequestParam("id") String id, HttpServletResponse httpServletResponse) {
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		Clock obj = null;
		boolean flag = false;
		for (Thread thread : threads) {
			if (s.contains(thread.getName())) {
				Clock clock = clockTimers.get(thread.getName());
				thread.stop();
				clock.setModifiedTime(LocalTime.now());
				clock.setStatus(Status.STOPPED);
				clockTimers.replace(id, clock);
				obj = clock;
				flag = true;
			}
		}
		if (!flag) {
			return "Error id: " + id + " not present";
		} else {
			return "Clear" + "id:" + id + "\n" + obj;
		}
	}

	@RequestMapping("/pause")
	public String pauseId(@RequestParam("id") String id, HttpServletResponse httpServletResponse) {
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		boolean flag = false;
		Clock clock = clockTimers.get(id);
		for (Thread thread : threads) {
			if (clock.getName().equals(thread.getName())) {
				thread.suspend();
				clock.setModifiedTime(LocalTime.now());
				clock.setStatus(Status.PAUSED);
				clockTimers.replace(id, clock);
				System.out.println(clock);
				flag = true;
			}
		}
		if (! flag) {
			return "Error id: " + id + " not present";
		}else {
			return "Pause" + "id: " + id + "\n" + clock;
		}
	}
}
