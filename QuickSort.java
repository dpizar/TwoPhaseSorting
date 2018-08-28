

public class QuickSort {
	
	/**
	 * Partition the sublist
	 * @param arr
	 * @param start
	 * @param end
	 * @return
	 */
	private int partition(String[] arr, int start, int end) {

		String temp=arr[start];
		int id=Integer.parseInt(temp.substring(0, 7));
		
		while (start<end) {
			while (start<end && id<=Integer.parseInt(arr[end].substring(0,7))) {
				end--;
			}
			arr[start]=arr[end];
			
			while (start<end && id>=Integer.parseInt(arr[start].substring(0, 7))) {
				start++;
			}
			arr[end]=arr[start];
		}
		arr[start]=temp;
		return start;
	}
	
	/**
	 * Sort by recursion
	 * @param arr
	 * @param start
	 * @param end
	 */
	public void sorting(String[] arr,int start,int end){
			
			if (start<end) {
				int key=partition(arr,start,end);
				sorting(arr, start, key-1);
				sorting(arr, key+1, end);
			}		
	}

}
