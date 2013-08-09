package csc316project2;

/**
 * This class is designed to store line and position coordinates of a word within a text file
 * @author Gabe
 * @version 11/3/10
 */
public class Point {
	private int line;
	private int position;
	
	/**
	 * Constructor - Location of a word in a text file as (line, position)
	 * @param line line number of a word occurrence
	 * @param position position in a line of a word occurrence
	 */
	public Point(int line, int position) {
		this.line = line;
		this.position = position;
	}
	
	/**
	 * Returns line number of a word occurrence
	 * @return line number of a word occurrence
	 */
	public int getLine() {
		return this.line;
	}
	
	/**
	 * Returns position of word in a line
	 * @return position of a word in a line
	 */
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Prints a point as a string
	 * @return [line#, position]
	 */
	public String toString() {
		return "[" + line + ", " + position + "]";
	}
}
