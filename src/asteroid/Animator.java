package asteroid;

import asteroid.gui.Frame;
import asteroid.model.Universe;

public class Animator extends Thread {
	private Universe universe;
	private Frame frame;
	private boolean running = true;
	private boolean waiting = true;

	public Animator(Universe universe, Frame frame) {
		super();
		this.universe = universe;
		this.frame = frame;
		this.setDaemon(true);
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	public boolean isWaiting() {
		return waiting;
	}

	@Override
	public void run() {
		while(running) {
			while (waiting) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			universe.anime();
			frame.refreshView();
		}
	}

}
