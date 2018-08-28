import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		int numberOfSubListed;
		int memorySize = 0;
        String fileName = "";
        
        //Read number of blocks
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Block Numbers>>");
        memorySize=keyboard.nextInt();
              
        System.out.println("Please Enter Number of Tuples in file>>");
        int numberOfTuples=keyboard.nextInt();
        
        PhaseOne p1 = new PhaseOne();
        PhaseTwo P2= new PhaseTwo();
        p1.totalTuples=numberOfTuples;
        P2.nOfTuples=numberOfTuples;

        System.out.println("File Path>> ");
        BufferedReader enterr;
        enterr = new BufferedReader(new InputStreamReader(System.in));
        fileName = enterr.readLine();

        //Perform Phase 1 by creating sorted sublists
        
        p1.PhaseOne_CreatedSortedSublists(memorySize, fileName);
        System.out.println("Starting Phase I..");
        System.out.println("Phase I finished.");
        //Get the number of runs in phase1
        numberOfSubListed=p1.getRun();
        //Start Phase 2
        System.out.println("Starting Phase II..");
        //Merge sorted sublists
        
        P2.mergeSubLists(numberOfSubListed, memorySize);
        System.out.println("Phase II finished.");
        
	}
}
