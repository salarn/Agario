package Buffers;

import java.util.ArrayList;
import java.util.List;

public class GameStatusOutBuffer {
	
	List <String> status;
	MyWaitNotify monitor;
	
	
	public GameStatusOutBuffer() {
		status  = new ArrayList<>();
		monitor = new MyWaitNotify(this);
	}
	
	public void put ( List <String> statusMoment ){
		Clear();
		status.addAll(statusMoment);
		monitor.Notify();
	}
	
	public List<String> pick () {
		monitor.Wait();
		synchronized (this){
			if(isEmpty()) {
				return null;
			}
			return status;
		}
	}
	
	public synchronized boolean isEmpty() {
		return status.isEmpty();
	}
	
	public synchronized void Clear() {
		status.clear();
	}
}
