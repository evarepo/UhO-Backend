package com.uhoh.uhohws.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueService {

	private static BlockingQueue queue = new ArrayBlockingQueue(1024);
	
	public static void enqueue(Object obj) throws InterruptedException{
		queue.put(obj);
	}
	
	public static Object dequeue(){
		return queue.poll();
	}
}
