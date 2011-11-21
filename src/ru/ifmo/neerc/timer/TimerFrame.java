package ru.ifmo.neerc.timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pcms2.services.site.Clock;
import ru.ifmo.neerc.gui.ImagePanel;

@SuppressWarnings("serial")
public class TimerFrame extends TimerGUI {
	private JLabel timeLabel = new JLabel();
	private ImagePanel panelBgImg;
	private boolean frozen = false;
	class TimerJFrame extends JFrame {
		TimerJFrame() {
			super("PCMS2 Timer");

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
			cTime = new AtomicReference<SynchronizedTime>();
			cTime.set(new SynchronizedTime(0, true));
			status = new AtomicInteger(Clock.BEFORE);

			// Do some magic
			boolean f = frozen;
			setFrozen(false);
			setFrozen(true);
			setFrozen(f);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					TimerJFrame.this.setVisible(true);
				}

			}).start();
		}
	}
	
	TimerFrame() {
		new TimerJFrame();
	}

	@Override
	protected void repaint() {
		
	}

	@Override
	protected void setText(String text, Color c) {
		if (timeLabel != null) {
			timeLabel.setText(text);
			timeLabel.setForeground(c);
		}
	}

	@Override
	protected void setFrozen(boolean b) {
		if (frozen == b) {
			return;
		}
		
		frozen = b;
		
		if (panelBgImg != null) {
			if (frozen) {
				panelBgImg.setBackground(new ImageIcon("frozen.jpg").getImage());
			} else {
				panelBgImg.setBackground(new ImageIcon("background.jpg").getImage());
			}
		}
	}
}
