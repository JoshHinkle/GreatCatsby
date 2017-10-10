package com.joshhinkle.catsbyAdventure.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;

public class Score implements Serializable {
	/**
	 * Creates a Score
	 * @param score the number of points for that game run 
	 */
	public Score(int score) {
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;

	}

	/**
	 * Saves the score to a file using Serialization
	 */
	public void serialize() {
		try {
			ArrayList<Score> scores = getAllScoresSorted();
			scores.add(this);
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(scores);
			out.close();
		} catch (IOException e) {
			System.out.println("Problem Serializing Data");
			System.out.println(e.getMessage());
		}
	}

	
	/**
	 * Gets all the scores from previous games
	 * @return a sorted array list of previous games.
	 */
	public static ArrayList<Score> getAllScoresSorted(){
		ArrayList<Score> allTopScores = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			allTopScores = (ArrayList<Score>) in.readObject();
			in.close();
		} catch (IOException e) {
			System.out.println("Problem reading the file, IO exception");
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Problem reading the file, ClassNotFound exception");
			e.printStackTrace();
		}
		
		Comparator<Score> comparator = new Comparator<Score>(){

			@Override
			public int compare(Score s1, Score s2) {
				return s2.getScore() - s1.getScore();
			}
		};
		if(allTopScores!=null){
			allTopScores.sort(comparator);
		}
		else{
			allTopScores =  new ArrayList<Score>();
			allTopScores.add(new Score(100));
			allTopScores.add(new Score(100));
			allTopScores.add(new Score(100));
			allTopScores.add(new Score(100));
			allTopScores.add(new Score(100));
		}
		return allTopScores;
	}
	
	/**
	 * Determine if Scores are equal based on the score
	 */
	@Override
	public boolean equals(Object that){
		if(that!=null){
			return this.score==((Score) that).getScore();
		}
		else return false;
	}
	

	private int score;
	private static final File file = Gdx.files.local("resources/high_scores").file();

}
