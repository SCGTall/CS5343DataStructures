/**
 * FileName: SortBySwapNode.java
 * Introduction:
 * DO NOT USE ANY LIBRARIES TO MANIPULATE LISTS
 * THE SORTING ALGORITHM IS THE ONE WE HAVE COVERED IN THE CLASS.
 *
 * you can all functions to create and insert list nodes from the main.
 *
 * 1. Create a single linked list of at least 15 nodes.  The numbers in the list should not be sorted.
 * traverse the list.
 *
 * 2. Sort the list in ascending order. Sort in such a manner that the node is unlinked and re-linked at the correct location.
 * traverse the list - it should show the sorted order.
 *
 * submit the code
 * submit screen shot of the traversal before and after sorting
 *
 * Thought:
 * 1. I will realize a single linked list first with function of traverse and swap. For no libraries can be used to munipulate lists.
 * 2. I will use random method to establish an unsorted list.
 * 3. Then I will use the idea of selection sort, but swap nodes this time.
 * 4. All function is static since we want to do all in the main.
 *
 * @author Chaoran Li
 * @Date 09/12/2019
 * @version 1.0
 */
package sortBySwapNode;

import java.util.Random;

public class SortBySwapNode {
    public static final int LENGTH = 30;// length of linked list , LENGTH >= 15
    public static final int RANGE = 100;// element start from 0, and end to RANGE
    /**
     * element is an int
     */
    public static class MyNode{
        private int element;
        private MyNode next;
        public MyNode(int inElement) {
            next = null;
            element = inElement;
        }
    }

    /**
     * single linked list start from head, and end when next == null
     */
    public static class MySingleLinked {
        private MyNode head;
        public MySingleLinked(){
            head = null;
        }
        public void traverse(){
            MyNode node = head;
            System.out.printf("[");
            while(node != null){
                if (node.next == null){
                    System.out.printf("%d", node.element);
                }
                else{
                    System.out.printf("%d, ", node.element);
                }
                node = node.next;
            }
            System.out.printf("]\n");
        }
        // swap node1 and node2, beforeNode can be null if the node is head
        public void swap(MyNode node1, MyNode beforeNode1, MyNode node2, MyNode beforeNode2){
            if ((node1 == null) || (node2 == null) ||(node1 == node2)) return;
            //traverse();
            //System.out.printf("%d %d\n", node1.element, node2.element);
            // swap point to node
            if (beforeNode1 == null){// node1 is head
                head = node2;
                beforeNode2.next = node1;
            }
            else if (beforeNode2 == null) {// node2 is head
                head = node1;
                beforeNode1.next = node2;
            }
            else{
                beforeNode1.next = node2;
                beforeNode2.next = node1;
            }
            // swap node point to
            MyNode tmp = node1.next;
            node1.next = node2.next;
            node2.next = tmp;
        }
        public void swapNodeSort(){
            MyNode beforeNode1 = null;
            MyNode node1 = head;// swap's input node1: the first element in rest list
            while(node1 != null){
                MyNode beforeNode2 = node1;
                MyNode node2 = beforeNode2.next;// swap's input node2: the smallest element in rest list
                MyNode beforeTmp = beforeNode2;
                MyNode tmp = beforeTmp.next;// iterator
                while (tmp != null){
                    if (tmp.element < node2.element){
                        beforeNode2 = beforeTmp;
                        node2 = beforeNode2.next;
                    }
                    beforeTmp = tmp;
                    tmp = beforeTmp.next;
                }
                if ((node2 != null) && (node1.element > node2.element)){
                    swap(node1, beforeNode1, node2, beforeNode2);
                    beforeNode1 = node2;// next one has changed!
                }
                else{
                    beforeNode1 = node1;
                }
                node1 = beforeNode1.next;
            }
        }
    }

    /**
     * Create a single linked list of lenth inLen. Each node have a random value from 0 to 100;
     * @param len length of linked list
     * @return a single linked list
     */
    public static MySingleLinked createSingleLinkedList(int len){
        MySingleLinked linked = new MySingleLinked();
        Random rand = new Random();// a random number generator
        MyNode node = new MyNode(rand.nextInt(RANGE));
        linked.head = node;
        MyNode tail = node;
        for (int i = 1; i < len; i++){// inLen >= 15 > 1
            node = new MyNode(rand.nextInt(RANGE));
            tail.next = node;
            tail = node;
        }
        return linked;
    }

    public static void main(String[] args) {
        MySingleLinked linked = createSingleLinkedList(LENGTH);// 1. create a single linked list
        System.out.printf("Before sort:\n");
        linked.traverse();
        linked.swapNodeSort();
        System.out.printf("After sort:\n");
        linked.traverse();
    }

}