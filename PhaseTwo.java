
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileInputStream;



public class PhaseTwo {
	
	public  int nOfTuples;
	private boolean isFinished=false;
	private int totalNumberOfSubList;
	private int tuplesPerMemBlock;
	private BufferedReader[] br;
	private int[] counterEOF;
	private boolean[] runFlag;
	private int[] counter;
	private int numberOfTuplesPerBlock= 40 ;  
	private int numberOfInputOutPut=0;
	private int numberOfRounds=0;
	private int memoryBlock;
	
	/**
	 * Merge Sub lists
	 * @param nOfSublists
	 * @param memoryBlock
	 * @throws IOException
	 */
	public void mergeSubLists(int nOfSublists,int memoryBlock) throws IOException
	{
		this.memoryBlock=memoryBlock;
		totalNumberOfSubList=nOfSublists;
		//Calculate the number of buffers that we'll use to merge the sublists(number of blocks input at the beginning.)
		tuplesPerMemBlock=(int)Math.floor((double)(memoryBlock-1)*40/nOfSublists);
		counterEOF=new int[totalNumberOfSubList];
		counter=new int[totalNumberOfSubList];
		runFlag=new boolean[totalNumberOfSubList];
		//set the counterEOF variables to know where all the sublist end.
		for (int i = 0; i < totalNumberOfSubList; i++) {
			if (i+1==totalNumberOfSubList) {
				if (nOfTuples%(numberOfTuplesPerBlock*memoryBlock)!=0) 
					counterEOF[i]=nOfTuples%(40*memoryBlock);
				else
					counterEOF[i]=numberOfTuplesPerBlock*memoryBlock;
			}else 
				counterEOF[i]=numberOfTuplesPerBlock*memoryBlock;
			
			counter[i]=0;
			runFlag[i]=true;	
		}
		
		br=new BufferedReader[totalNumberOfSubList];
		//Store all the sublist in an array, so they can be read later on.
		for (int i = 0; i < br.length; i++)
        {
			DataInputStream in=new DataInputStream(new FileInputStream("sublist"+(i+1)+".txt"));
			br[i]=new BufferedReader(new InputStreamReader(in));
		}
		String[][] temp=new String[totalNumberOfSubList][tuplesPerMemBlock];
		
		int i=0;
		//Beginnin to read the data from the sublist into memory.
		for (; i < totalNumberOfSubList; i++) {
			for (int j = 0; j < tuplesPerMemBlock; j++) {
				String st=null;
				if ((st=br[i].readLine())!=null) {
					temp[i][j]=st;
					counterEOF[i]--;
				}
			}
		}
		
		findMin(temp);
	}

/**
 * find Min
 * @param tempFile
 */
	private void findMin(String[][] tempFile) 
	{
		PrintWriter pr=null;
		PrintWriter rangepr=null;
		String rangeStr=null;		
		try {
			pr=new PrintWriter("MergedSublist.txt");
			rangepr=new PrintWriter("RangeTable.txt");
			
			long startTime=System.currentTimeMillis();
			long startMemory=Runtime.getRuntime().freeMemory();
			
			int tupleCounter=0;
			//Merge until finished.
			while (true) 
			{
				String minimum=null;
				
				//Find the minimun value.
				for (int j = 0; j < totalNumberOfSubList; j++) {
					if (runFlag[j])
					{
						if (minimum==null)
							minimum=tempFile[j][counter[j]];
						else
							minimum=findMinmumm(minimum,tempFile[j][counter[j]]);
					}
				}
				
				pr.println(minimum);
				
				if (tupleCounter==0)
					rangeStr="minimum  "+minimum.substring(0, 7)+"  ";
				if (tupleCounter==39) {
					rangeStr=rangeStr+"maximum "+minimum.substring(0, 7);
					rangepr.println(rangeStr);
					tupleCounter=-1;
				}
				tupleCounter++;
				
				for (int k = 0; k < totalNumberOfSubList; k++) 
				{
					if (minimum.equalsIgnoreCase(tempFile[k][counter[k]])) 
					{
						if (counter[k]==tuplesPerMemBlock-1) 
						{
							counter[k]=0;
							String CurrentLine;
							
							for (int k2 = 0; k2 < tuplesPerMemBlock; k2++) 
							{
								if (k2==0) {
									if ((CurrentLine=br[k].readLine())==null) 
									{
										runFlag[k]=false;
										break;
									}else 
									{
										tempFile[k][k2]=CurrentLine;
										counterEOF[k]--;
									}
								}else 
								{
									if ((CurrentLine=br[k].readLine())!=null)
									{
										tempFile[k][k2]=CurrentLine;
										counterEOF[k]--;
									}else 
									{
										tempFile[k][k2]=null;
									}
								}								
							}//for
						}else 
						{
							if (tempFile[k][counter[k]+1]==null) 
								runFlag[k]=false;
							else
								counter[k]++;
						}
					}
				}		
				//Set boolean isFnished to true if merginf is completed.
				checkForEnd();
				//numberOfRounds++;
				if (isFinished) {
					break;
				}				
			}
			//40--for tuples per block 100--for size of each tuple.
			numberOfRounds=nOfTuples*100/(this.memoryBlock*40*100);
			System.out.println("Number of Rounds for phase2:   "+numberOfRounds);
			//2-- for written and readen.
			this.numberOfInputOutPut=(2*numberOfRounds)*(nOfTuples/40);
			
			System.out.println("Number of I/O for Phase2:"+numberOfInputOutPut);
			//Nullify our pointers
			tempFile=null;
			counter=null;
			br=null;
			runFlag=null;
			counterEOF=null;
			System.gc();
			
			System.out.println("time taken for phase 2 ::"+(System.currentTimeMillis()-startTime)+"ms");
			System.out.println("memory used for phase 2 ::"+(Runtime.getRuntime().totalMemory()-startMemory)+"bytes");
			
			pr.close();rangepr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Find minimum Employee ID
	 * @param line1
	 * @param str2
	 * @return
	 */
	private String findMinmumm(String line1, String line2) 
	{
		if (Integer.parseInt(line1.substring(0, 7))<Integer.parseInt(line2.substring(0, 7))) 
			return line1;		
		else
			return line2;
	}

	
	/**
	 * Check if we are at the end of file
	 */
	private void checkForEnd() 
	{
		for (int i = 0; i < totalNumberOfSubList; i++) {
			if (runFlag[i]) {
				isFinished=false;
				break;
			}
			isFinished=true;
		}
		
	}
}

