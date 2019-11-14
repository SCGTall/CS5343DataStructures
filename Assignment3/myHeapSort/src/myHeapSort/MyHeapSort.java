/**
 * FileName: MyHeapSort.java
 * Introduction:
 * Implement heap sort.
 * You are given an array of N positive numbers, in a random order.
 *
 * 1. Make the array into a heap.  A null node is indicated by -1. The -1 (null node) could be anywhere in the array,
 * i.e. all -1 are not at the end of array.
 * 2. Sort the heap using heap sort.
 *
 * Submit the code.
 * run your program on an array of 20 random numbers.  You may hard code to initialize the  array in the main
 * submit screen shots showing the initial array
 * a screen shot showing the array after it is concerted to a heap
 * a screen shot showing the sorted array.
 *
 * Thought:
 * 1. I will generate an array of 20 random numbers first.
 * 2. I will init an array based heap tree and copy all not -1 numbers, from index of 1.
 * 3. Then I do the heapify according to the rules of the maximum heap.
 * 4. In the end, I will do a heap sort, and the numbers should be shown in ascending order.
 *
 * @author Chaoran Li
 * @Date 10/18/2019
 * @version 1.0
 */
package myHeapSort;
import java.util.Arrays;
import java.util.Random;

public class MyHeapSort {
    public static final int LEN = 20;// array of 20
    public static enum HeapType{
        max,
        min,
    }

    public static void main(String[] args) {
        int origin[] = generateRandomArray(0.2, 0, 100);
        System.out.printf("Initial array: ");
        System.out.println(Arrays.toString(origin));

        int typeNum = HeapType.max.ordinal();
        // init heap
        int heap[] = new int[LEN + 1];
        Arrays.fill(heap, -1);
        heap[0] = 0;
        int heapIndex = 1;
        for (int num : origin){
            if (num == -1) continue;;
            heap[heapIndex] = num;
            heapIndex++;
            heap[0]++;
        }
        //System.out.printf("Start:\t\t   ");
        //System.out.println(Arrays.toString(heap));

        // heapify the array
        for (int i = heap[0] / 2; i > 0; i--){
            heapify(heap, i, HeapType.values()[typeNum]);
        }
        System.out.printf("Convert to a heap: ");
        System.out.println(Arrays.toString(heap));

        // heap sort
        int validC = heap[0];// heap[0] will change after heap sort
        while (heap[0] > 1){
            mySwap(heap, 1, heap[0]);
            heap[0]--;
            heapify(heap, 1, HeapType.values()[typeNum]);
        }
        System.out.printf("Heapsorted array: ");
        System.out.println(Arrays.toString(Arrays.copyOfRange(heap, 1, validC)));
    }

    /**
     * heapify
     * @param heap array-based heap
     * @param index the index
     * @param type HeapType
     */
    private static void heapify(int[] heap, int index, HeapType type) {
        int left = 2 * index;
        int right = left + 1;
        //System.out.printf("Step: (%02d, %02d) ", index, heap[index]);
        //System.out.println(Arrays.toString(heap));
        // classify by number of valid
        try{
            if (right <= heap[0]){// two valid
                if (myCompare(heap[left], heap[right], type)){// use left
                    if (myCompare(heap[index], heap[left], type)) return;
                    mySwap(heap, index, left);
                    heapify(heap, left, type);
                }
                else {
                    if (myCompare(heap[index], heap[right], type)) return;
                    mySwap(heap, index, right);
                    heapify(heap, right, type);
                }
            }
            else if (left <= heap[0]){// one valid
                if (myCompare(heap[index], heap[left], type)) return;
                mySwap(heap, index, left);
                heapify(heap, left, type);
            }
            else{}
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * compare a and b according to type
     * @param a
     * @param b
     * @param type a enum of HeapType
     * @return
     * @throws Exception undefined HeapType
     */
    private static boolean myCompare(int a, int b, HeapType type) throws Exception {
        switch (type.ordinal()){
            case 0:
                return a >= b;
            case 1:
                return a <= b;
            default:
                throw new Exception("Undefined HeapType: " + type.toString());
        }
    }

    /**
     * swap a[i] and a[j]
     * @param a
     * @param i
     * @param j
     */
    private static void mySwap(int[] a, int i, int j){// swap
        a[i] += a[j];// a = a0 + b0, b = b0
        a[j] = a[i] - a[j];// a = a0 + b0, b = a0
        a[i] -= a[j];// a = b0, b = a0
    }

    /**
     * get a random array
     * @param prob probability of -1 (null node)
     * @param startNum start number of range
     * @param endNum end number of range
     * @return
     */
    private static int[] generateRandomArray(double prob, int startNum, int endNum) {
        int output[] = new int[LEN];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < LEN; i++){
            if (random.nextDouble() < prob){
                output[i] = -1;
            }
            else{
                output[i] = random.nextInt(endNum - startNum) + startNum;
            }
        }
        return output;
    }

}
