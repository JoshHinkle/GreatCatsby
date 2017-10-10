package com.joshhinkle.catsbyAdventure.tools;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.badlogic.gdx.Gdx;

/**
 * UI Runnable to open game info in separate JFrame 
 * Accessed by user pressing "i" on Splash 
 * Producer Thread reads the file and puts each line in a Queue 
 * Consumer Thread takes the strings from the queue and adds them to the UI component 
 * @author Josh
 *
 */
public class InfoRunnable implements Runnable {

	private final File info = Gdx.files.local("resources/info.txt").file();

	@Override
	public void run() {

		// define UI components
		final BoundedQueue<String> queue = new BoundedQueue<String>(35);
		final JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(450,415));
		final JTextArea textArea = new JTextArea();
		
		// define a producer and consumer runnable
		Runnable producerRunnable = new Runnable() {

			@Override
			public void run() {
				FileReader fr;
				try {
					fr = new FileReader(info);
					BufferedReader br = new BufferedReader(fr);
					String nextLine = br.readLine();
					while (nextLine != null && !queue.isFull()) {
						queue.add(nextLine);
						nextLine = br.readLine();
					}
					br.close();
					fr.close();
				} catch (FileNotFoundException e) {
					System.out.println("Cannot find info file");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable consumerRunnable = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					String nextLine;
					if (!queue.isEmpty()) {
						nextLine = queue.remove();
						textArea.append(nextLine + "\n");
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		// Start them off
		
		Thread producerThread = new Thread(producerRunnable);
		Thread consumerThread = new Thread(consumerRunnable);

		producerThread.setPriority(9);
		consumerThread.setPriority(1);
		producerThread.start();
		consumerThread.start();

		
		frame.add(textArea);
		frame.setVisible(true);
		frame.pack();
	}

}
