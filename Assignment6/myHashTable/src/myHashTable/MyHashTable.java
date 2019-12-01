/**
 * FileName: MyHashTable.java
 * Introduction:
 * For this assignment, you will create a hash table to implement spell checker.
 * 1. Create a file of 100 words of varying length (max 8 characters).
 * 2. All the words are in lower case.
 * 3. design a hash table.
 * 4. enter each word in the hash table.
 *
 * Once the hash table is created, run it as a spell checker.
 * You enter a word (interactive), find the word in your hash table.
 * If not found enter an error message.  Then prompt for next word.  End the session by some control character like ctrl-c or something.
 * 1. use the linear probing first.  Count the number of collisions and print it.
 * 2. Then use quadratic probing.  Count the number of collisions and print it.
 * 3. Start with a table size that is about 53.  So 100 words would have a load factor of more than .5.
 * your program should increase table size accordingly.
 * Now add 10 more words.  The program should automatically determine that table size needs to increase.
 * Print out - increasing table size to <size>
 * rehash all the old words also and the new words to the new hash table.
 * You already have linear probing and quadratic probing functions.  Print the collisions in each case for the new table size.
 *
 * Thought:
 * 1. I will first input wordList.txt to get words.
 * 2. Then I will design DefaultHashTable with two subclassed: MyLinearProbingHT and MyQuadraticProbingHT. Probing method will be realize in subclasses.
 * 3. Design add method for hash table. Report collisions and rehash if necessary automatically.
 * 4. Design find method for hash table.
 * 5. Main method contains three steps:
 * 1) Use first 100 words to create hash tables;
 * 2) Add 10 more words;
 * 3) Run as a spell checker. Enter "/exit" (exit command) if you want to break.
 * 6. Do not consider dirty digit, for there is not deletion in this assignment.
 *
 * Announcement:
 * I made a 148-word wordList.txt for input using the information I found randomly. It was shuffled, space-free and obeyed the rules above.
 * This list should be only used for this study instead of commercial purposes.
 *
 * @author Chaoran Li
 * @Date 11/30/2019
 * @version 1.0
 */

package myHashTable;

import java.io.*;
import java.util.Scanner;

public class MyHashTable {
    private static enum HashProbingMethod{
        undefined,
        linear,
        quadratic;
    }
    private static final int STARTSIZE = 53;// start table size = 32
    private static final int CHARACTERNUM = 26;// lower case and space-free.
    private static final int STEPONENUM = 100;// step 1:100
    private static final int STEPTWONUM = 110;// step 2: 100 + 10
    private static final String EXITCOMMAND = "/exit";// exit command

    /**
     * abstract class. You should always use subclasses with complete methods instead of this.
     */
    private static abstract class DefaultHashTable{
        public String[] dictionary = null;// values
        protected HashProbingMethod method = HashProbingMethod.undefined;// probing method
        protected int size = 0;// table size
        protected int number = 0;// element number

        /**
         * probing method
         * @return enum value of method
         */
        public HashProbingMethod getProbingMethod(){
            return method;
        }

        /**
         * get load factor
         * @return lambda
         */
        public double getLoadFactor(){
            return ((float)number / (float)size);
        }

        /**
         * get hash value
         * 26 ^ 8 = 2.1E11 > 2.1E9 = 2^31 - 1. So we should use long when calculate value. But we will return an int.
         * @param word new words
         * @return hash value
         */
        public int getHashValue(String word){
            int digit = 0;
            long value = 0;
            while (word.length() > digit){
                int tmp = (int)word.charAt(digit) - (int)'a';
                value = value * CHARACTERNUM + (long)tmp;
                digit++;
            }
            return (int)(value % size);
        }

        /**
         * use No.(i-1) value to get No.i value
         * @param lastValue last value
         * @param i
         * @return next value
         */
        public abstract int getNextIndex(int lastValue, int i);

        /**
         * standfard find method for find and add.
         * @param word target word
         * @return return target index if word is found. Return next empty index that can be add into if word is not found.
         */
        private int findWord(String word){
            int value = getHashValue(word);
            int i = 1;
            int index = value;
            // if collision happens
            while (!word.equals(dictionary[index])){// continue find. Be careful about string equal
                // break condition
                if (null == dictionary[index]) {// empty judge
                    break;
                }
                // loop
                index = getNextIndex(index, i);
                i++;
            }
            return index;
        }

        /**
         * add method
         * @param word target method
         */
        public void add(String word){
            if (HashProbingMethod.undefined == method) {// security
                System.out.print("\nUndefinedd Probing Method!\n");
                return;
            }
            // existed?
            int index = findWord(word);
            if (word.equals(dictionary[index])) return;// existed

            int original = getHashValue(word);
            if (index != original){// collision happened
                System.out.printf("Collision: " + word + "\t%d -> %d\n", original, index);// report collision
            }
            dictionary[index] = word;
            number++;
        };

        /**
         * find method
         * @param word target word
         * @return return target index if word is found. Return next empty index that can be add into if word is not found.
         */
        public int find(String word){
            if (HashProbingMethod.undefined == method) {// security
                System.out.print("\nUndefinedd Probing Method!\n");
                return -1;
            }

            return findWord(word);
        };
    }

    /**
     * Linear Probing
     */
    private static class MyLinearProbingHT extends DefaultHashTable{
        public MyLinearProbingHT(int tableSize){
            size = tableSize;
            dictionary = new String[size];
            method = HashProbingMethod.linear;
        }

        /**
         * override for linear probing
         * @param lastValue last value
         * @param i
         * @return
         */
        @Override
        public int getNextIndex(int lastValue, int i) {
            return (lastValue + 1) % size;
        }
    }

    /**
     * Quadratic Probing
     */
    private static class MyQuadraticProbingHT extends DefaultHashTable{
        public MyQuadraticProbingHT(int tableSize){
            size = tableSize;
            dictionary = new String[size];
            method = HashProbingMethod.quadratic;
        }

        @Override
        public int getNextIndex(int lastValue, int i) {
            return (lastValue + 2 * i - 1) % size;
        }
    }

    public static void main(String[] args) {
        // input wordList.txt
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        try{
            File file = new File(MyHashTable.class.getResource("/wordList.txt").getFile());
            br1 = new BufferedReader(new FileReader(file));// br1 -> lpht
            br1.mark((int)file.length());
            br2 = new BufferedReader(new FileReader(file));// br2 -> qpht
            br2.mark((int)file.length());

            String tmp = null;
            MyLinearProbingHT lpht = new MyLinearProbingHT(STARTSIZE);
            MyQuadraticProbingHT qpht = new MyQuadraticProbingHT(STARTSIZE);
            // step one: 100 words
            System.out.println("Step 1: Use first 100 words to create two hash tables.");
            System.out.println("Linear Probing:");
            while (lpht.number < STEPONENUM){
                tmp = br1.readLine().trim();
                if (null == tmp) break;// security
                lpht.add(tmp);
                if (0.5 < lpht.getLoadFactor()){// rehash
                    rehash(lpht);
                    br1.reset();
                    lpht.number = 0;
                    continue;
                }
            }
            System.out.println("\nQuadratic Probing:");
            while (qpht.number < STEPONENUM){
                tmp = br2.readLine().trim();
                if (null == tmp) break;// security
                qpht.add(tmp);
                if (0.5 < qpht.getLoadFactor()){// rehash
                    rehash(qpht);
                    br2.reset();
                    qpht.number = 0;
                    continue;
                }
            }
            // step two: 10 more words
            System.out.println("\nStep 2: Add 10 more words.");
            System.out.println("Linear Probing:");
            while (lpht.number < STEPTWONUM){
                tmp = br1.readLine().trim();
                if (null == tmp) break;// security
                lpht.add(tmp);
                if (0.5 < lpht.getLoadFactor()){// rehash
                    rehash(lpht);
                    br1.reset();
                    lpht.number = 0;
                    continue;
                }
            }
            System.out.println("\nQuadratic Probing:");
            while (qpht.number < STEPTWONUM){
                tmp = br2.readLine().trim();
                if (null == tmp) break;// security
                qpht.add(tmp);
                if (0.5 < qpht.getLoadFactor()){// rehash
                    rehash(qpht);
                    br2.reset();
                    qpht.number = 0;
                    continue;
                }
            }
            // step three: run as a spell checker
            System.out.println("\nStep 3: Now run as a spell checker. ");
            System.out.println("(max 8 characters, lower case and space-free)\n");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input your word:");
            tmp = scanner.nextLine();
            while (!tmp.equals(EXITCOMMAND)){
                if (!isValidInput(tmp)){// security
                    System.out.println("Unvalid input.\n\nInput your word:");
                    tmp = scanner.nextLine();
                    continue;
                }
                int index1 = lpht.find(tmp);
                if (!tmp.equals(lpht.dictionary[index1])){// not found
                    System.out.println("Not found in hash tables.\n\nInput your word:");
                    tmp = scanner.nextLine();
                    continue;
                }
                int index2 = qpht.find(tmp);
                if (!tmp.equals(qpht.dictionary[index2])){// not found
                    System.out.println("Not found in hash tables.\n\nInput your word:");
                    tmp = scanner.nextLine();
                    continue;
                }
                System.out.printf("Linear Probing:%d, Quadratic Probing:%d\n\nInput your word:\n", index1, index2);
                tmp = scanner.nextLine();
            }
            System.out.println("Exit! GLHF!");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                br1.close();
                br2.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    /**
     * judge if input is valid
     * @param word target word
     * @return true for valid and false for unvalid
     */
    private static boolean isValidInput(String word) {
        if (word.length() > 8) return false;
        int tmp = 0;
        while (tmp < word.length()){
            char c = word.charAt(tmp);
            if (((int)c < (int)'a') || ((int)c > (int)'z')) return false;
            tmp++;
        }
        return true;
    }

    /**
     * rehash method
     * @param ht target hash table
     */
    private static void rehash(DefaultHashTable ht) {
        ht.size = getNextPrime(ht.size * 2);
        ht.dictionary = new String[ht.size];
        System.out.printf("Increase table size to <%d>\n", ht.size);
    }

    /**
     * table size should be prime
     * @param num target number
     * @return least prime larger than target number
     */
    private static int getNextPrime(int num) {
        int outp = num;
        while (!isPrime(outp)){
            outp++;
        }
        return outp;
    }

    /**
     * judge if it is a prime number
     * @param n target number
     * @return true for prime number; false for composite number
     */
    private static boolean isPrime(int n) {
        if (n <= 1) return false;// prime number should greater than 1
        int tmp = 2;
        while (tmp * tmp < n){
            if (0 == n % tmp) return false;
            tmp++;
        }
        return true;
    }
}
