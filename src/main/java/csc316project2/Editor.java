package csc316project2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is designed to run a search and replace operation 
 * given input and output textfiles.
 * @author Gabe
 * @version 11/2/10
 */
public class Editor {
	private final static Logger LOGGER = Logger.getLogger(Editor.class.getName()); // http://www.vogella.de/articles/Logging/article.html
	private ArrayList<ArrayList<String>> lineBin;
	private TreeMap<String, WordNode> wordMap;
	private int numLines;
	//private int numWords;
	private File outputFile;
	
	/**
	 * Constructor - initializes an Editor object
	 * @param outputFile file to write input file w/ replacements
	 */
	public Editor(String outputFile) {
		//this.textLine = new ArrayList<String>();
		this.lineBin = new ArrayList<ArrayList<String>>();
		this.wordMap = new TreeMap<String, WordNode>();
		this.numLines = 0;
		//this.numWords = 0;
		this.outputFile = new File(outputFile);
	}
	
// Getters	
	/**
	 * Getter - accesses the lineBin attribute
	 * @return lineBin
	 */
	public ArrayList<ArrayList<String>> getLineBin() {
		return lineBin;
	}
	/**
	 * Getter - accesses the wordMap attribute
	 * @return wordMap
	 */
	public TreeMap<String, WordNode> getWordMap() {
		return wordMap;
	}
	/**
	 * Getter - accesses the numLines attribute
	 * @return number of lines in the input file
	 */
	public int getNumLines() {
		return numLines;
	}
	
	/**
	 * Getter - accesses the numWords attribute
	 * @return number of distinct words in the input file and wordMap
	
	public int getNumWords() {
		return numWords;
	} */
	
	/**
	 * Increments the numLines in input file
	 */
	public void incrementLineCount() {
		numLines++;
	}
	
	/**
	 * Adds a line of tokens to the arrayList lineBin
	 * @param line (line number) and/or (lineBin index)
	 * @param text ArrayList of strings to add to lineBin
	 */
	public void addLine(int lineNum, ArrayList<String> text) {
		lineBin.add(lineNum, text);
	}
	
	/**
	 * Populates the treeMap and increments word counter 
	 * for each unique word in the input line
	 * @param line a line from the input textfile
	 */
	public void populateTreeMap(String line, int lineNum) {
		//String word;
		int position = 0;
		ArrayList<String> text = new ArrayList<String>();
		StringTokenizer tokens;
		// scan each line for words
		//Scanner lineScanner = new Scanner( line );
		//lineScanner.useDelimiter("[\\<]+");
		
		// add words to treeMap or update locations
		//while ( lineScanner.hasNext() ) {
			//word = lineScanner.nextLine();
			
			tokens = new StringTokenizer( line, "~`!@#$%^&*()-+={[]}|\\:;<,.>/?\'\" ", true); 
			LOGGER.info("There are " + tokens.countTokens() + " tokens");
			String word = "";
				
			// add each word (including punctuation) to arrayList: 
			while (tokens.hasMoreTokens()) {
				word = tokens.nextToken();
				// if (!word.equalsIgnoreCase(" ")) { // do not store space chatacters in data structures
				LOGGER.info( word );
				text.add( word );
				position++;
					
				// add word to wordMap
				if ( wordMap.containsKey( word) ) {
					wordMap.get(word).addOccurrence(lineNum, position);
				}
				else {
					wordMap.put(word, new WordNode(word, lineNum, position));
					//numWords++;
				}//}
			}
			//text.add(word);
			//position++;
		//}
		addLine(lineNum, text);
		
	}
	
	/**
	 * Tests to see if user input is legal or terminate program for Q
	 * @param command string of user input tokens
	 * @return true if user input is in proper format 
	 * 		   otherwise, prints error message and terminates program
	 */
	public  boolean validCommand( String[] command) {
		// User wants to quit
		if (command[0].equalsIgnoreCase("" + "Q") ) {
			LOGGER.info("Compose output file and terminate execution");
			composeOutFile();
			System.exit(0);
			return true;
		}
		
		// Check for 3 arguments in command
		else if (command.length != 3 ) {
			System.out.print("Illegal command <" );
			for (int i=0; i<command.length; i++) {
				System.out.print(command[i] + " ");
			}
			System.out.println(">");
			System.exit(1);
	    }
		
		// Check for command R
		if (!command[0].equalsIgnoreCase("" + "R")) {
			System.out.println("R requires two arguments - you have "
					+ command.length);
			System.exit(1);
	    }
		
		// Check for illegal words
		else if (command[1].contains("[:punct:]") ) {
			System.out.println("Illegal word " + command[1]);
			System.exit(1);
	
		}
		else if (command[2].contains("[:punct:]") ) {
			System.out.println("Illegal word " + command[2]);
			System.exit(1);
		}
		
		return true;	// input is in proper format
	}

	/**
	 * Returns the WordNode from the wordMap, with key that matches the input parameter
	 * @param word key of WordNode to return
	 * @return WordNode if in wordMap, null otherwise
	 */
	public WordNode search(String word) {
		if ( wordMap.containsKey(word) )
			return wordMap.get(word);
		else
			return null;
	}

	/**
	 * Convert an ArrayList of Strings into one output String
	 * @param linebin ArrayList of String tokens
	 * @return a console friendly String of ArrayList elements (w/o brackets or commas)
	 */
	public String printArrayList( ArrayList<String> linebin) {
		String string = "";
		Iterator<String> out = linebin.iterator();
		while( out.hasNext()) {
			string += out.next();
		}
		return string;
	}
	
	/**
	 * Replaces each occurrence of the searchWord with the ReplacementWord in the input file
	 * @param searchWord key to look for in the wordMap
	 * @param locations WordNode containing the searchWord and its locations in the input file
	 * @param replacementWord word to replace all occurrences of searchWord
	 */
	public void replaceAll(String searchWord, WordNode locations, String replacementWord) {
		Point rep;
		ArrayList<Integer> line = new ArrayList<Integer>();
		int i = 0;	// line# counter
		//String line;
		
		// for each line to be replaced...
		for (Iterator<Point> p = locations.getOccurrences().iterator(); p.hasNext(); ) {
			rep = p.next(); 
			line.add( Integer.valueOf(rep.getLine()) );	// track line numbers in array
			LOGGER.info("Line #" + line);
			if (i == 0) {
				System.out.println("< " + printArrayList(lineBin.get(rep.getLine())));
				Collections.replaceAll(lineBin.get(rep.getLine()), searchWord, replacementWord);
				System.out.println("> " + printArrayList(lineBin.get(rep.getLine())));
			}
			else if ( !line.contains(Integer.valueOf(rep.getLine())))  {
				// get line using occurrences [line, position] from WordNode
				//line = lineBin.get(rep.getLine()).get(rep.getPosition());
				//LOGGER.info(line);
				// print line before replacement
				System.out.println("< "
						+ printArrayList(lineBin.get(rep.getLine())));
				// replace word in string
				Collections.replaceAll(lineBin.get(rep.getLine()), searchWord, replacementWord);
				//.set(rep.getPosition()-1, replacementWord);
				// print line after replacement
				System.out.println("> "
						+ printArrayList(lineBin.get(rep.getLine())));
			}
		}
		
		// create a new WordNode with same occurrences and counter
			WordNode replacement = wordMap.remove(searchWord);
			
		if (wordMap.containsKey(replacementWord)) {
			Point temp;
			
			for (Iterator<Point> more = replacement.getOccurrences().iterator(); more.hasNext(); ) {
				temp = more.next();
				wordMap.get(replacementWord).addOccurrence(temp.getLine(), temp.getPosition());
			}
			
			//updateNode(red, red.getOccurrences());
		}
		else {
			
			// remove old WordNode
			replacement.setKeyword(replacementWord);
			
			// place new wordnode back into treeMap or update a pre-existing one
			wordMap.put(replacementWord, replacement);
		}
		i++;
		LOGGER.info(wordMap.toString());
	}

	/**
	 * Adds word locations from a replaced WordNode to an existing WordNode in the wordMap
	 * @param update the WordNode whose locations need to be transferred to an existing replacement WordNode
	 * @param additions the locations ArrayList of Points that need to be added to the existing replacement WordNode 
	
	public void updateNode(WordNode update, ArrayList<Point> points) {
		
		
		
	} */
	
	/**
	 * Prints all replacements made from the input file to the output file. 
	 * Does not change input file
	 */
	public void composeOutFile() {
		try {
			FileWriter fw = new FileWriter(outputFile);
			BufferedWriter buffer = new BufferedWriter(fw);
			PrintWriter output = new PrintWriter(buffer);
			String line;
			for (Iterator<ArrayList<String>> it = lineBin.iterator(); it.hasNext(); ) {
			    for (Iterator<String> s = it.next().iterator(); s.hasNext(); ) {
			    	line = s.next();
			    	output.print(line );
			    	System.out.print(line);
			    }
			    output.println();
			    System.out.println();
			}
			output.close();
		} catch (IOException e) {
			System.out.println("Error outputting to file: " + e);
			System.exit(1);
		}
	}
	
	/**
	 * Runs the search and replace algorithm given input and output files
	 * @param args [1] input_file [2] output_file
	 */
	public static void main(String[] args) {
		LOGGER.setLevel(Level.FINEST);	// toggles debugging printout
		
		// verify correct number of command line arguments
		if (args.length != 2) {
			System.out.println("Usage: java <input_file> <output_file>");
			System.exit(1);
		}
		
		Editor editor = new Editor( args[1] );	// initializes an Editor object
		
		// initialize data structures with input_file 
		try {	
			File inputFile = new File(args[0]);
			Scanner fileScanner = new Scanner(inputFile);
			
			// populate data structures with file contents
			String buffer;
			while( fileScanner.hasNext() ) {
				buffer = fileScanner.nextLine();
				editor.populateTreeMap( buffer, editor.getNumLines() );
				editor.incrementLineCount();
			}
			LOGGER.info(editor.getLineBin().toString() );
			LOGGER.info(editor.wordMap.toString());
		}
		catch (FileNotFoundException e) {
			System.err.println("File does not exist!");
			System.exit(1);
		}
		
		// get commands from user
		Scanner console = new Scanner(System.in);
		WordNode result;

		while ( console.hasNext() ) {
			// String command = console.next();
			StringTokenizer tokens;		// line from scanner
			String[] command = null; 	// array of input tokens
			
			// place user input into an array as tokens
			if ( (tokens = new StringTokenizer( console.nextLine(), "~`!@#$%^&*()-+={[]}|\\:;<,.>/?\'\" ", false )) != null) {
				command = new String[tokens.countTokens()];		// creatr array of proper size for tokens
				LOGGER.info("There are " + tokens.countTokens() + " tokens");
				for (int j = 0; j < command.length; j++){
					command[j] = tokens.nextToken();
				}
				LOGGER.info(Arrays.toString(command));
			}
			
			// test command array for proper arguments
			if ( editor.validCommand( command) ) {	// command[ <command>, <searchWord>, <replacementWord> ]
				LOGGER.info("The command was valid");
				
				// search for word
				result = editor.search( command[1] );
				if (result == null) {
					System.out.println("Word not found! PLEASE TRY AGAIN!");
					continue;
				}
				LOGGER.info(result.toString());
				LOGGER.info("Now write the replace method and test.");
				
				// replace word
				editor.replaceAll(command[1], result, command[2]);
			}
			else 
				LOGGER.info("Invalid command should have exited program!");
		}
	}

}
