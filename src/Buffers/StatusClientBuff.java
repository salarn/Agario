package Buffers;

import java.util.ArrayList;
import java.util.List;

public class StatusClientBuff {
	private List <String> bufferLst;
	MyWaitNotify monitor;

	public StatusClientBuff (){
		bufferLst = new ArrayList<>();
		monitor   = new MyWaitNotify(this);
	}

	public void put(String str) {
		bufferLst.add(str);
		if ( str.compareTo("done") == 0 || str.compareTo("dead") == 0 )
			monitor.Notify();
	}

	public List<String> pick() {
		monitor.Wait();
		synchronized (this) {
			if(isEmpty()) {
				return null;
			}
			List <String> rtnLst = new ArrayList<>();
			rtnLst.addAll(bufferLst);
			clear();
			return rtnLst;
		}
	}

	public synchronized boolean isEmpty() {
		return bufferLst.isEmpty();
	}

	public synchronized void clear() {
		bufferLst.clear();
	}
}
