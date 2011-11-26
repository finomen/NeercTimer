package ru.ifmo.neerc.timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
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
	private JFrame frame;
	AtomicReference<String> text;
	AtomicReference<Color> color;
	
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
			status = new AtomicInteger(Clock.BEFORE);

			// Do some magic
			boolean f = frozen;
			setFrozen(false);
			setFrozen(true);
			setFrozen(f);

			text = new AtomicReference<String>();
			color = new AtomicReference<Color>();
			
			TimerJFrame.this.setVisible(true);
		}
		
		@Override
		public void paint(Graphics g) {
			Color c = color.get();
			String t = text.get();
			if (c != null && t != null) {
				int fsize = 1000;
				Font f = new Font("Calibri", Font.BOLD, fsize);
	
				if (timeLabel != null) {
					while (timeLabel.getFontMetrics(f).stringWidth(t) > timeLabel
							.getWidth() - 20
							|| timeLabel.getFontMetrics(f).getHeight() > timeLabel
									.getHeight()) {
						fsize -= 5;
						f = new Font("Calibri", Font.BOLD, fsize);
					}
					
					timeLabel.setFont(f);
					timeLabel.setText(t);
					timeLabel.setForeground(c);
					frame.repaint();
				}
				
				color.compareAndSet(c, null);
				text.compareAndSet(t, null);
			}			
			super.paint(g);
		}
	}

	TimerFrame() {
		frame = new TimerJFrame();
	}

	@Override
	protected void repaint() {
		timeLabel.repaint();
	}

	@Override
	protected void setText(String t, Color c) {
		this.text.set(t);
		this.color.set(c);
		frame.repaint();
	}

	@Override
	protected void setFrozen(boolean b) {
		if (frozen == b) {
			return;
		}

		frozen = b;

		if (panelBgImg != null) {
			if (frozen) {
				panelBgImg
						.setBackground(new ImageIcon("frozen.jpg").getImage());
			} else {
				panelBgImg.setBackground(new ImageIcon("background.jpg")
						.getImage());
			}
		}
	}
}
