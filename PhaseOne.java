

	import java.io.BufferedReader;
	import java.io.DataInputStream;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.PrintWriter;
	import java.io.UnsupportedEncodingException;


	public class PhaseOne 
	{
		public  int totalTuples;
		private int freememory=0;
		private int numberOfTuplesPerBlock= 40 ; 
		private int numberOfInputOutPut=0;
		private static int numberOfSublists;
		
   
		/**
		 * This method create sorted sublists as a first phase of PMMS
		 * @param bolckSize
		 * @param fileName
		 * @throws FileNotFoundException
		 * @throws UnsupportedEncodingException
		 * @throws IOException
		 */
		public void PhaseOne_CreatedSortedSublists(int bolckSize , String fileName ) 
				throws FileNotFoundException, UnsupportedEncodingException, IOException {
			
			//Create a temporary sublist to hold data
			String[] temp=new String[numberOfTuplesPerBlock*bolckSize];
			//Get free memory available
			long startMemory=Runtime.getRuntime().freeMemory();
			//Create a buffer reader on the file that contains the records
			BufferedReader br=new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))));
			//Set free memory
			freememory=numberOfTuplesPerBlock*bolckSize;
			//Set number of sublists
			numberOfSublists=totalTuples/ freememory; 			
			//Set startTime
			long startTime=System.currentTimeMillis();
			
			
			// fill in the temporary buffers
			for (int i = 1; i <= numberOfSublists; i++)
			{
				if (i==numberOfSublists) //Set the new size
				{
					if (totalTuples%(numberOfTuplesPerBlock*bolckSize)!=0) 
							temp=new String[totalTuples%(40*bolckSize)];
					else
							temp=new String[numberOfTuplesPerBlock*bolckSize];
				}
				else
					//Set the temporary buffer size with the tuples total size 
					temp=new String[numberOfTuplesPerBlock*bolckSize];
				
				for (int j = 0; j < temp.length; j++)
				{
					temp[j]=readFromFile(br);
				}
				//For each file since we make one I/O per each block and each block contains 40 tuples=4K,
				//The number of I/O is==> (numberOfTuplesPerBlock*bolckSize)/40.
				this.numberOfInputOutPut=this.numberOfInputOutPut+(numberOfTuplesPerBlock*bolckSize)/40;
				//start sorting
				QuickSort sort=new QuickSort();
				sort.sorting(temp, 0, temp.length-1);
				//Output sublists to file
				writeSublistsToFile("sublist"+i+".txt", temp);
				//Calculate the number Of I/O when writing to file.
				this.numberOfInputOutPut=this.numberOfInputOutPut+(numberOfTuplesPerBlock*bolckSize)/40;
			}
			
			System.out.println("Number of I/O operations for phase one: "+this.numberOfInputOutPut);
			//Set the endTime in millisecond
			long endTime=System.currentTimeMillis();
			temp=null;			
			//Set the run time memory available after sorting operations
			long endMemory=Runtime.getRuntime().totalMemory();
			//Display data to screen
			System.out.println("time for phase 1 ::" +(endTime-startTime)+" ms");
			System.out.println("memory used for phase 1 ::"+(endMemory-startMemory)+" bytes");			
		}


		/**
		 * 
		 * @param filename
		 * @param lines
		 * @throws FileNotFoundException
		 * @throws UnsupportedEncodingException
		 */
		private static void writeSublistsToFile(String filename,String[] lines) 
				throws FileNotFoundException, UnsupportedEncodingException{
			
			PrintWriter print=new PrintWriter(filename,"ISO-8859-1");
			for (String str:lines)			
				print.println(str);			
			print.close();
		}
		
		/**
		 * Read Lines From File
		 * @param bufferreader
		 * @return
		 */
		private static String readFromFile(BufferedReader bufferreader){
			try {
				String line;
				while ((line=bufferreader.readLine())!=null) {
					return line;
				}
			} catch (Exception e) {
				//Do Nothing
			}
			return "";
		}
		
		/**
		 * Get the number of runs
		 * @return
		 */
		public int getRun()
	        {
	            return numberOfSublists; 
	        }
		
		

	}



