package Buffers;

import java.util.ArrayList;
import java.util.List;

public class ServerToFrameBuff {
	
	private List <String> bufferLst;
	MyWaitNotify monitor;
	
	public ServerToFrameBuff (){
		bufferLst = new ArrayList<>();
		monitor   = new MyWaitNotify(this);
	}
	
	public void put(String str) {
		bufferLst.add(str);
		monitor.Notify();
	}
	
	public String pick() {
		monitor.Wait();
		synchronized (this) {
			if(isEmpty()) {
				return null;
			}
			String res=bufferLst.get(0);
			bufferLst.remove(0);
			return res;
		}
	}
	
	public synchronized boolean isEmpty() {
		return bufferLst.isEmpty();
	}
	
	public synchronized void clear() {
		bufferLst.clear();
	}
}
