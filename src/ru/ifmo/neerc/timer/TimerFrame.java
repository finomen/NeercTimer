package ru.ifmo.neerc.timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;

import javax.sql.rowset.spi.SyncResolver;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ru.ifmo.neerc.gui.ImagePanel;
import pcms2.services.site.Clock;

public class TimerFrame extends JFrame {
	public static final int BEFORE = 0;
	public static final int RUNNING = 1;
	public static final int FROZEN = 3;
	public static final int PAUSED = 4;
	public static final int LEFT5MIN = 5;
	public static final int LEFT1MIN = 6;
	public static final int OVER = 7;
	
	private JLabel timeLabel = new JLabel();
	private ImagePanel panelBgImg;
	private Integer status;
	private Long cTime, cDelta;
	private Color[] palette;

	TimerFrame() {
		super("PCMS2 Timer");
		palette = new Color[8];
		for (int i = 0; i < 8; ++i)	{
			palette[i] = Color.BLACK;
		}
		
		this.setUndecorated(true);
		setBounds(0, 0, 1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane();
		timeLabel.setFont(new Font("Calibri", Font.BOLD, 350));
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setVerticalAlignment(JLabel.CENTER);
		timeLabel.setForeground(Color.green);
		con.setBackground(Color.BLACK);

		timeLabel.setText("0:00:00");
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		setSize(xSize, ySize);

		con.setLayout(null);
		panelBgImg = new ImagePanel();
		con.add(panelBgImg);
		
		panelBgImg.setLayout(new BorderLayout());
		panelBgImg.add(timeLabel, BorderLayout.CENTER);
		panelBgImg.setBounds(0, 0, xSize, ySize);
		cTime = Long.valueOf(0);
		cDelta = Long.valueOf(0);
		status = Integer.valueOf(0);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				TimerFrame.this.setVisible(true);
				
			}
			
		}).start();
		
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				long timestamp = Calendar.getInstance().getTime().getTime();
				while (true) {
					long nts = Calendar.getInstance().getTime().getTime();
					long diff = nts - timestamp;
					timestamp = nts;
					long dtime = 0;
					synchronized (cDelta) {
						synchronized (cTime) {
							long correction = Math.min(cDelta, diff / 4);
							cTime = cTime - diff + correction;
							cDelta = cDelta - correction;
							dtime = cTime;
						}
					}
					
					Date cd = new Date(dtime);
					String text = null;
					Color c = null;
					synchronized (status) {
						switch (status) {
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
					}
					
					if (cd.getMinutes() <= 1) {
						c = palette[LEFT1MIN];
					} else if (cd.getMinutes() <= 5) {
						c = palette[LEFT5MIN];
					}
					
					if (cd.getHours() > 0) {
						text = String.format("%d:%02d:%02d", cd.getHours(), cd.getMinutes(), cd.getSeconds());
					} else if (cd.getMinutes() > 0) {
						 
						text = String.format("%02d:%02d", cd.getMinutes(), cd.getSeconds());
					} else {
						text = String.format("%d", cd.getSeconds());
					}
					
					timeLabel.setText(text);
					timeLabel.setForeground(c);
				}

			}
			
		}).start();
		
	}
	
	public void setStatus(int status) {
		synchronized (this.status) {
			this.status = Integer.valueOf(status);
			this.repaint();
		}
	}
		
	public void sync(long time) {
		synchronized (this.cDelta) {
			synchronized (this.cTime) {
				cDelta = time - this.cTime;
				if (cDelta >= 3000) {
					cDelta = Long.valueOf(0);
					this.cTime = time;
				}
				this.repaint();
			}
		}
	}
}
