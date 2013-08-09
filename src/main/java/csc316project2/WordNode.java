package csc316project2;

import java.util.ArrayList;

/**
 * This class created and manages word nodes to be stored within a TreeMap
 * 		WordNodes contain a word (key), an ArrayList to hold occurrences 
 * 		within a file, and a counter for the number of times the word occurs in the file
 * @author Gabe
 * @version 11/3/10
 */
public class WordNode {
	private String keyword;
	private ArrayList<Point> occurrences;
	private int counter;

	/**
	 * Constructor - creates and initializes WordNode objects
	 * @param keyword word that serves as a key in a TreeMap
	 * @param line line number within a text file where the keyword occurs
	 * @param position the token number of the word on the line above
	 */
	public WordNode(String keyword, int line, int position) {
		this.keyword = keyword;
		this.occurrences = new ArrayList<Point>();
		occurrences.add(new Point(line, position));
		this.counter = 1;
	}
	
	/**
	 * Getter - gets the WordNode's keyword
	 * @return the WordNode's keyword
	 */
	public String getKeyword(){
		return this.keyword;
	}
	
	/**
	 * Getter - gets the WordNode's ArrayList of occurrences
	 * @return the WordNode's ArrayList of occurrences
	 */
	public ArrayList<Point> getOccurrences() {
		return this.occurrences;
	}
	
	/**
	 * Getter - gets the counter for number of occurrences of the keyword
	 * @return number of occurrences of the keyword
	 */
	public int getCounter() {
		return this.counter;
	}

	/**
	 * Adds a Point object to the WordNode containing line and position information for a word
	 * @param line line number within a text file where the keyword occurrs
	 * @param position the token number of the word on the line above
	 */
	public void addOccurrence(int line, int position) {
		occurrences.add(new Point(line, position));
		counter++;
	}
	
	/**
	 * Setter - sets the keyword of a WordNode
	 * @param keyword key to be used in a TreeMap
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	/**
	 * Overrides the toString method to return information about a WordNode for debugging purposes
	 */
	public String toString() {
		return "\nWORDNODE: " + keyword + "\n" + 
					"\t" + "Locations: " + occurrences + "\n" + 
					"\t" + "Occurrences: " + counter + "\n";
	}
	
	/**
	 * A testing main with sample code for class methods
	 * @param args
	 */
	public static void main(String[] args) {
		WordNode test = new WordNode("Atomic", 1, 12);
		test.addOccurrence(2, 5);
		test.addOccurrence(9, 22);
		System.out.println(test);
		System.out.println(test.getOccurrences());
		
	}
}