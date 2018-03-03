package tomcat_web_dev;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("deprecation")
public class HelloApplet extends Applet implements Runnable {
	
	private int fontSize = 8;
	private Thread changer;
	private boolean stopFlag = true;
	private Button contrlButton = new Button(" Begin the dynamic display! ");
	
	public void init() {
		contrlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stopFlag) {
					start();
				} else {
					stop();
				}
			}
		});
		
		setBackground(Color.WHITE);
		add(contrlButton);
		setSize(100, 100);
	}
	
	public void start() {
		changer = new Thread(this);
		stopFlag = false;
		fontSize = 8;
		contrlButton.setLabel(" Stop dynamic display ");
		changer.start();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("newFont", Font.BOLD, fontSize));
		g.drawString("Hello", 30, 80);
	}
	
	public void stop() {
		stopFlag = true;
		contrlButton.setLabel(" Begin dynmaic display! ");
	}

	@Override
	public void run() {
		while(!stopFlag) {
			repaint();
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			if(fontSize++ > 40) {
				fontSize = 8;
			}
		}
		
	}
	
}
