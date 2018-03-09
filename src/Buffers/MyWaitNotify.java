package Buffers;


public class MyWaitNotify {
	Object Monitor;
	int Signalled = 0;

	public MyWaitNotify(Object monitor) {
		this.Monitor = monitor;
	}

	public MyWaitNotify() {
		this.Monitor = new Object();
	}

	public void Wait() {
		synchronized (Monitor) {
			while (Signalled == 0) {
				try {
					Monitor.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Signalled--;
		}
		// clear signal and continue running.
	}

	public void Notify() {
		synchronized (Monitor) {
			Signalled++;
			Monitor.notify();
		}
	}
}