package noiseremoving;

/*
SortArray Class - Generic array sorting utility using QuickSort algorithm

QUESTION 1: Is quick sort the best way of finding median? Why?
No, QuickSort is not the best way for finding median. Because
- Time Complexity: QuickSort has O(n log n) average time complexity
- For finding median, we only need the middle element, not a fully sorted array
- QuickSort sorts the entire array, which is overkill for median finding
- We're doing extra work by sorting elements we don't need

QUESTION 2: What is another good way of finding median? Please provide explanation.
Better approaches for finding median include:
1. QUICKSELECT ALGORITHM:
- Time Complexity: O(n) average case, O(n²) worst case
- Uses partitioning like QuickSort but only recurses on one side
- Finds the kth smallest element without fully sorting
- For median, k = n/2 (middle position)
 
2. MEDIAN OF MEDIANS ALGORITHM:
- Guaranteed O(n) time complexity
- Divides array into groups of 5, finds median of each group
- Uses median of medians as pivot for guaranteed performance

3. HEAP-BASED APPROACH:
- Use two heaps (max-heap for smaller half, min-heap for larger half)
- Time Complexity: O(n log n) but good for streaming data
 
4. FOR SMALL ARRAYS (like our 9-pixel case):
- Simple insertion sort or selection sort might be faster due to low overhead
- Network sorting or hardcoded comparisons for fixed-size arrays


 */

/*
Name: Suemon Kwok

Student ID: 14883335

Data structures and algorithms
*/

public class SortArray<T extends Comparable<T>> {
    
    /**
     * Array field to store reference to the array being sorted
     */
    private T[] array;
    
    /**
     * Default constructor
     */
    public SortArray() {
        this.array = null;
    }
    
    /*
    Constructor with array parameter
     
    @param array The array to be sorted
     */
    public SortArray(T[] array) {
        this.array = array;
    }
    
    /*
    Sets the array reference to be sorted
    
    @param array The array to be sorted
     */
    public void setArray(T[] array) {
        this.array = array;
    }
    
    /*
    Gets the current array reference
    
    @return The current array being sorted
     */
    public T[] getArray() {
        return this.array;
    }
    
    /*
    Public method to initiate QuickSort on the entire array
    
    Sorts the array in ascending order
     */
    public void quickSort() {
        if (array == null || array.length <= 1) {
            return; // Nothing to sort
        }
        quickSort(0, array.length - 1);
    }
    
    /*
    Private recursive QuickSort implementation
    
    @param low Starting index of the subarray to sort
    
    @param high Ending index of the subarray to sort
     */
    private void quickSort(int low, int high) {
        if (low < high) {
            // Partition the array and get the pivot index
            int pivotIndex = partition(low, high);
            
            // Recursively sort elements before and after partition
            quickSort(low, pivotIndex - 1);    // Sort left subarray
            quickSort(pivotIndex + 1, high);   // Sort right subarray
        }
    }
    
    /*
    Partitions the array around a pivot element
    
    Uses the last element as pivot (Lomuto partition scheme)
    
    @param low Starting index of the subarray
    
    @param high Ending index of the subarray (pivot position)
    
    @return The final position of the pivot after partitioning
     */
    private int partition(int low, int high) {
        // Choose the last element as pivot
        T pivot = array[high];
        
        // Index of smaller element, indicates right position of pivot
        int i = low - 1;
        
        // Traverse through array and compare each element with pivot
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (array[j].compareTo(pivot) <= 0) {
                i++; // Increment index of smaller element
                swap(i, j); // Swap elements
            }
        }
        
        // Swap pivot element with element at (i + 1)
        swap(i + 1, high);
        
        return i + 1; // Return the position of pivot
    }
    
    /*
    Swaps two elements in the array
    
    @param i Index of first element
    
    @param j Index of second element
     */
    private void swap(int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /*
    Utility method to get the median value after sorting
    
    Assumes the array has been sorted
    
    @return The median value
     */
    public T getMedian() {
        if (array == null || array.length == 0) {
            return null;
        }
        
        // For our 9-pixel case, median is at index 4 (middle of 0-8)
        int medianIndex = array.length / 2;
        return array[medianIndex];
    }
    
    /*
    Convenience method to sort array and return median in one call
    
    @return The median value after sorting
     */
    public T sortAndGetMedian() {
        quickSort();
        return getMedian();
    }
}