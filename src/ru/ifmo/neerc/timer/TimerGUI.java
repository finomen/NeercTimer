package ru.ifmo.neerc.timer;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import pcms2.services.site.Clock;

public abstract class TimerGUI{
	public static final int BEFORE = 0;
	public static final int RUNNING = 1;
	public static final int FROZEN = 3;
	public static final int PAUSED = 4;
	public static final int LEFT5MIN = 5;
	public static final int LEFT1MIN = 6;
	public static final int OVER = 7;
	
	AtomicInteger status;
	AtomicReference<SynchronizedTime> cTime; 
	private Color[] palette;
	
	class SynchronizedTime {
		private final long cTime, sync;
		private long frozen, correction;
		public SynchronizedTime(long time, boolean frozen) {
			cTime = time;
			sync = System.currentTimeMillis();
			correction = 0;
			if (frozen)
				this.frozen = System.currentTimeMillis();
		}
		
		public long get() {
			if (frozen == 0) {
				return Math.max(0, cTime + sync - System.currentTimeMillis() + correction);
			} else {
				return Math.max(0, cTime + sync - frozen + correction);
			}
		}
		
		public void freeze() {
			if (frozen == 0) {
				frozen = System.currentTimeMillis();
			}
		}
		
		public void resume() {
			if (frozen != 0) {
				correction += System.currentTimeMillis() - frozen;
				frozen = 0;
			}	
		}
	}

	TimerGUI() {
		palette = new Color[8];
		for (int i = 0; i < 8; ++i)	{
			palette[i] = Color.BLACK;
		}
		
		setText("0:00:00", palette[BEFORE]);
		
		cTime = new AtomicReference<SynchronizedTime>();
		cTime.set(new SynchronizedTime(0, true));
		status = new AtomicInteger(Clock.BEFORE);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					long dtime = cTime.get().get();
					
					dtime /= 1000;
					int seconds = (int) (dtime % 60);
					dtime /= 60;
					int minutes = (int) (dtime % 60);
					dtime /= 60;
					int hours = (int) dtime;
					
					String text = null;
					Color c = null;
					switch (status.get()) {
						case Clock.BEFORE:
							c = palette[BEFORE];
							break;
						case Clock.RUNNING:
							c = palette[RUNNING];
							break;
						case Clock.OVER:
							c = palette[OVER];
							break;
						case Clock.PAUSED:
							c = palette[PAUSED];
							break;
					}
				
					if (minutes <= 1) {
						c = palette[LEFT1MIN];
					} else if (minutes <= 5) {
						c = palette[LEFT5MIN];
					} else if (hours == 0) {
						c = palette[FROZEN];
						setFrozen(true);
					} else {
						setFrozen(false);
					}
					
					if (hours > 0) {
						text = String.format("%d:%02d:%02d", hours, minutes, seconds);
					} else if (minutes > 0) {						 
						text = String.format("%02d:%02d", minutes, seconds);
					} else {
						text = String.format("%d", seconds);
					}
					
					setText(text, c);
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
							return;
					}
				}

			}
			
		}).start();
		
	}
	
	protected abstract void setFrozen(boolean b);

	protected abstract void setText(String string, Color c);
	protected abstract void repaint();

	public void setStatus(int status) {
		synchronized (this.status) {
			this.status.set(status);
			if (status != Clock.RUNNING) {
				cTime.get().freeze();
			} else {
				cTime.get().resume();
			}
			
			repaint();
		}
	}
		
	public void sync(long time) {
		cTime.set(new SynchronizedTime(time, status.get() != Clock.RUNNING));
		this.repaint();
	}
}
