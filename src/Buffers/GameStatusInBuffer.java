package Buffers;

public class GameStatusInBuffer {
	
	private int getCnt;
	MyWaitNotify monitor;
	
	
	public GameStatusInBuffer() {
		getCnt = 0;
		monitor = new MyWaitNotify(this);
	}
	
	public void put (){
		++getCnt;
		monitor.Notify();
	}
	
	public boolean pick () {
		monitor.Wait();
		synchronized(this){
			if ( getCnt == 0 )
				return false;
			return ( (getCnt--) > 0 );
		}
	}
}
