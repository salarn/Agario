package Buffers;

import java.util.ArrayList;
import java.util.List;

public class GameCommandBuffer {

	List <String> command;
	MyWaitNotify  monitor;
	
	public GameCommandBuffer() {
		command = new ArrayList<>();
		monitor = new MyWaitNotify(this);
	}
	
	public void put ( String s ){
		command.add(s);
		monitor.Notify();
	}
	
	public String pick () {
		monitor.Wait();
		synchronized (this){
			if(isEmpty()) {
				return null;
			}
			String query = command.get(0);
			command.remove(0);
			return query;
		}
	}
	
	public synchronized boolean isEmpty() {
		return command.isEmpty();
	}
	
	public synchronized void Clear() {
		command.clear();
	}
}
