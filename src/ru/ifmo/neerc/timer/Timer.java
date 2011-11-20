package ru.ifmo.neerc.timer;

import ru.ifmo.neerc.connectors.Pcms2;
import ru.ifmo.neerc.framework.Callback;



public class Timer implements Runnable{
	private long length, time;
	
	
	public static void main(String[] args) {		
		new Timer().run();
	}

	@Override
	public void run() {
		final TimerFrame tf = new TimerFrame();
		Pcms2 connection = new Pcms2("http://localhost:8080/pcms/party");
		connection.hookLengthChange(new Callback<Long>() {
			@Override
			public void exec(Long arg) {
				length = arg;
				tf.sync(length - time);
			}
		});
		connection.hookTimeChange(new Callback<Long>() {
			@Override
			public void exec(Long arg) {
				time = arg;
				tf.sync(length - time);
			}
		});
		connection.hookStatusChange(new Callback<Integer>() {
			@Override
			public void exec(Integer arg) {
				tf.setStatus(arg);
			}
		});
	}
} 
