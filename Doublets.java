import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import java.util.stream.Collectors;

/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 * @author Dev Patel (dzp0074@auburn.edu)
 */
public class Doublets implements WordLadderGame {

    // The word list used to validate words.
    // Must be instantiated and populated in the constructor.
    /////////////////////////////////////////////////////////////////////////////
    // DECLARE A FIELD NAMED lexicon HERE. THIS FIELD IS USED TO STORE ALL THE //
    // WORDS IN THE WORD LIST. YOU CAN CREATE YOUR OWN COLLECTION FOR THIS     //
    // PURPOSE OF YOU CAN USE ONE OF THE JCF COLLECTIONS. SUGGESTED CHOICES    //
    // ARE TreeSet (a red-black tree) OR HashSet (a closed addressed hash      //
    // table with chaining).
    /////////////////////////////////////////////////////////////////////////////
   TreeSet<String> lexicon;
    /**
     * Instantiates a new instance of Doublets with the lexicon populated with
     * the strings in the provided InputStream. The InputStream can be formatted
     * in different ways as long as the first string on each line is a word to be
     * stored in the lexicon.
     */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
                new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toLowerCase());
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }


    //////////////////////////////////////////////////////////////
    // ADD IMPLEMENTATIONS FOR ALL WordLadderGame METHODS HERE  //
    //////////////////////////////////////////////////////////////
    
   public int getWordCount() {
      return lexicon.size();
   }
    
   public boolean isWord(String str) {
      if (lexicon.contains(str)) {
         return true;
      }
      return false; 
   }
    
   public int getHammingDistance(String str1, String str2) {
      int distance = 0;
      
      if (str1.length() != str2.length()) {
         return -1;
      }
      for (int i = 0; i < str1.length(); i++) {
         if (str1.charAt(i) != str2.charAt(i)) {
            distance++;
         }
      }
      return distance; 
   }
    
   public List<String> getNeighbors(String word) {
      List<String> neighbor = new ArrayList<String>();
      
      for (String s : lexicon) {
         if (getHammingDistance(s, word) == 1) {
            neighbor.add(s);
         }
      }
      return neighbor;
   }
    
   public boolean isWordLadder(List<String> sequence) {
      if (sequence.isEmpty() || sequence == null) {
         return false;
      }
      
      if (sequence.size() == 1) {
         return true;
      }
      for (int i = 0; i < sequence.size() - 1; i++) {
         if (!(isWord(sequence.get(i + 1)) && isWord(sequence.get(i)))) {
            return false;
         }
         if (getHammingDistance(sequence.get(i + 1), sequence.get(i)) != 1) {
            return false;
         }
      }  
      return true;
   }
   
   public List<String> getMinLadder(String start, String end) {
      List<String> ladder = new ArrayList<String>();
      List<String> empty = new ArrayList<>();
      if (start.equals(end)) {
         ladder.add(start);
         return ladder;
      }
      else if (start.length() != end.length()) {
         return empty;
      }
      else if (!isWord(start) || !isWord(end)) {
         return empty;
      }
      Deque<Node> dq = new ArrayDeque<>();
      TreeSet<String> one = new TreeSet<>();
      one.add(start);
      dq.addLast(new Node(start, null));
      
      while (!dq.isEmpty()) {
         Node n = dq.removeFirst();
         String position = n.position;
         
         for (String neighbor1 : getNeighbors(position)) {
            if (!one.contains(neighbor1)) {
               one.add(neighbor1);
               dq.addLast(new Node(neighbor1, n));
            }
            if (neighbor1.equals(end)) {
               Node m = dq.removeLast();
               
               while (m != null) {
                  ladder.add(0, m.position);
                  m = m.previous;
               }
               return ladder;
            }
         }
      }
      return empty;
   }
   
   
   private class Node {
      String position;
      Node previous;
      
      public Node(String pos, Node prev) {
         position = pos;
         previous = prev; 
      }  
   }

}
