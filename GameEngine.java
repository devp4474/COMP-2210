import java.io.BufferedReader;
import java.io.File;
import java.lang.Math;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.io.FileNotFoundException;
import java.util.LinkedList;


public class GameEngine implements WordSearchGame {
   private boolean isLexLoaded = false;
   
   //private TreeSet<String> lexicon = new TreeSet<String>();
   private TreeSet<String> lexicon;
   
   private List<Integer> path;
   private int length;
   private boolean[][] visited;
   
   // private String[][] board = new String[][] {
   // {"E", "E", "C", "A"},
   // {"A", "L", "E", "P"},
   // {"H", "N", "B", "O"},
   // {"Q", "T", "T", "Y"}
   // };
   private String[][] board;
   
   private final int maxNeighbors = 8;
   private int order;
   private ArrayList<Position> path2;
   private int height;
   private int width;
   private String presentWords;
   private SortedSet<String> allWords;
   
   //private int squareSize = 4;
   private int squareSize;
   
   public GameEngine() {
      lexicon = null;
      squareSize = 4;
      board = new String[][] {
         {"E", "E", "C", "A"},
         {"A", "L", "E", "P"},
         {"H", "N", "B", "O"},
         {"Q", "T", "T", "Y"}
         };
   
   }
   
   /** loads lexicon into a data structure
   
   */
   public void loadLexicon(String fileName) {
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      
      File file = new File(fileName);
      Scanner scan = null;
      try {
         lexicon = new TreeSet<String>();
         scan = new Scanner(file);
         while (scan.hasNext()) {
            lexicon.add(scan.next().toLowerCase());
         }
      }
      catch (FileNotFoundException e) {
         throw new IllegalArgumentException();
      }
      finally {
         if (scan != null) {
            scan.close();
         }
      }
      isLexLoaded = true;
   }
   
   public void setBoard(String[] letterArray) {
      if (letterArray == null) {
         throw new IllegalArgumentException();
      }
      
      double n = Math.sqrt((double)letterArray.length);
      if (n == Math.round(n)) {
         int m = (int) n;
         board = new String[m][m];
         for (int x = 0; x < m; x++) {
            for (int y = 0; y < m; y++) {
               board[x][y] = letterArray[x * m + y];
            }
         }
         squareSize = m;
      }
      else {
         throw new IllegalArgumentException();
      }
   
   }
   
   public String getBoard() {
      String stringBoard = "";
      for (int i = 0; i < squareSize; i++) {
         stringBoard += "\n| ";
         for (int k = 0; k < squareSize; k++) {
            stringBoard += board[i][k] + " ";
         }
         stringBoard += "|";
      }
      return stringBoard;
     
      // StringBuilder string = new StringBuilder();
      // for (int x = 0; x < squareSize; x++) {
         // for (int y = 0; y < squareSize; y++) {
            // string.append(board[x][y] + " ");
            // if (x == squareSize - 1) {
               // string.append("br ");
            // }
         // }
      // }
      // String s = string.toString();
      // return s;
   }
   
   public SortedSet<String> getAllScorableWords(int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (!isLexLoaded) {
         throw new IllegalStateException();
      }
      
      boolean[][] visited = new boolean[squareSize][squareSize];
      TreeSet<String> words = new TreeSet<String>();
      for (int x = 0; x < squareSize; x++) {
         for (int y = 0; y < squareSize; y++) {
            getWords(x, y, minimumWordLength, "", visited, words);
         }  
      }
      return words;
      
      // allWords = new TreeSet<String>();
      // LinkedList<Integer> wordAlg = new LinkedList<Integer>();
      // for (int i = 0; i < (squareSize * squareSize); i++) {
         // wordAlg.add(i);
         // if (isValidWord(toWord(wordAlg)) && toWord(wordAlg).length() >= minimumWordLength) {
            // allWords.add(toWord(wordAlg));
         // }
         // if (isValidPrefix(toWord(wordAlg))) {
            // boardSearch(wordAlg, minimumWordLength);
         // }
         // wordAlg.clear();
      // }
      // return allWords;
   }
   

   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (!isLexLoaded) {
         throw new IllegalStateException();
      }
      
      int score = 0;
      Iterator<String> itr = words.iterator();
      
      while (itr.hasNext()) {
         String hold = itr.next();
         int length = hold.length();
         
         if (length >= minimumWordLength && isValidWord(hold) && !isOnBoard(hold).isEmpty()) {
            score++;
            if (hold.length() > minimumWordLength) {
               score += hold.length() - minimumWordLength;
            }
         }
      }
      return score;
   }
   
   public boolean isValidWord(String wordToCheck) {
               
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (!isLexLoaded) {
         throw new IllegalStateException();
      }
      
      wordToCheck = wordToCheck.toLowerCase();
   
      if (lexicon.contains(wordToCheck)) {
         return true;
      }
      else {
         return false;
      }
   }
   
   public boolean isValidPrefix(String prefixToCheck) {
         
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (!isLexLoaded) {
         throw new IllegalStateException();
      }
      
      prefixToCheck = prefixToCheck.toLowerCase();
      String word = lexicon.ceiling(prefixToCheck);
      
      if (word.equals(prefixToCheck) || word.startsWith(prefixToCheck)) {
         return true;
      }   
      return false;
   }
   
   public List<Integer> isOnBoard(String wordToCheck) {
      if (wordToCheck.equals(null)) {
         throw new IllegalArgumentException();
      }  
      if (!isLexLoaded) {
         throw new IllegalStateException();
      }
      boolean[][] visited = new boolean[squareSize][squareSize];
      ArrayList<Integer> list = new ArrayList<Integer>();
      for (int x = 0; x < squareSize; x++) {
         for (int y = 0; y < squareSize; y++) {
            onBoardWords(x, y, "", wordToCheck, visited, list);
         }
      }
      return list;
      
      // LinkedList<Integer> aList = new LinkedList<Integer>();
      // List<Integer> path = wordBoardSearch(wordToCheck, aList, 0);
      // return path;
   }
   
   
   private void onBoardWords(int x, int y, String str, String target, boolean[][] dbs1, ArrayList<Integer> word) {
      dbs1[x][y] = true;
      str = str + board[x][y];
      if (str.equals(target)) {
         for (int i = 0; i < squareSize; i++) {
            for (int k = 0; k < squareSize; k++) {
               if (dbs1[i][k] == true) {
                  word.add(i * squareSize + k);
               }
            }
         }
      }
      if (target.startsWith(str)) {
         for (int row = x - 1; row <= x + 1 && row < squareSize; row++) {
            for (int col = y - 1; col <= y + 1 && col < squareSize; col++) {
               if (row >= 0 && col >= 0 && !dbs1[row][col]) {
                  onBoardWords(row, col, str, target, dbs1, word);
               }
            }
         }
      }
      str = str.substring(0, str.length() - 1);
      dbs1[x][y] = false;
   }
   
   public void getWords(int x, int y, int min, String str, boolean[][] dbs, TreeSet<String> word) {
      dbs[x][y] = true;
      str = str + board[x][y];
      
      if (isValidWord(str) && str.length() >= min) {
         word.add(str);
      }
      if (isValidPrefix(str)) {
         for (int row = x - 1; row <= x + 1 && row < squareSize; row++) {
            for (int col = y - 1; col <= y + 1 && col < squareSize; col++) {
               if (row > 0 && col > 0 && !dbs[row][col]) {
                  getWords(row, col, min, str, dbs, word);
               }
            }
         }
      }
      str = str.substring(0, str.length() - 1);
      dbs[x][y] = false;
   }
   
   
   private void markAllLeft() {
      visited = new boolean[width][height];
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }
   
   private void markVisited() {
      for (int i = 0; i < path2.size(); i++) {
         visit(path2.get(i));
      }
   }
   
   
   
   private boolean isValid(int xTest, int yTest) {
      return (xTest >= 0) && (xTest < squareSize) && (yTest >= 0) && (yTest < squareSize);
   }
   
   //private void process(int x, int y) {
      //grid[x][y] = order++;
   //}  
   
   private boolean isVisited(Position p) {
      return visited[p.x][p.y];
   } 
   
   private LinkedList<Integer> boardSearch(LinkedList<Integer> wordAlg, int min) {
      Position[] adjacentArray = new Position(wordAlg.getLast()).neighbors(wordAlg);
      for (Position p : adjacentArray) {
         if (p == null) {
            break;
         }
         wordAlg.add(p.getIndex());
         if (isValidPrefix(toWord(wordAlg))) {
            if (isValidWord(toWord(wordAlg)) && toWord(wordAlg).length() >= min) {
               allWords.add(toWord(wordAlg));
            }
            boardSearch(wordAlg, min);
         }
         else {
            wordAlg.removeLast();
         }
      }
      wordAlg.removeLast();
      return wordAlg;
   }
   
   private LinkedList<Integer> wordBoardSearch(String wordToCheck, LinkedList<Integer> listAlg, int pIndex) {
      if (listAlg.size() > 0 && !wordToCheck.equals(toWord(listAlg))) {
         Position[] adjacentArray = new Position(pIndex).neighbors(listAlg);
         for (Position p : adjacentArray) {
            if (p == null) {
               break;
            }
            listAlg.add(p.getIndex());
            if (wordToCheck.equals(toWord(listAlg))) {
               break;
            }
            if (wordToCheck.startsWith(toWord(listAlg))) {
               wordBoardSearch(wordToCheck, listAlg, p.getIndex());
            }
            else {
               listAlg.removeLast();
            }
         }
      }
      if (listAlg.size() == 0) {
         while (pIndex < (squareSize * squareSize)) {
            if (wordToCheck.startsWith(new Position(pIndex).getLetter())) {
               listAlg.add(pIndex);
               wordBoardSearch(wordToCheck, listAlg, pIndex);
            }
            pIndex++;
         }
         return listAlg;
      }
      if (toWord(listAlg).equals(wordToCheck)) {
         return listAlg;
      }
      else {
         listAlg.removeLast();
         return listAlg;
      }
   }
   
   public String toWord(LinkedList<Integer> inputList) {
      String word = "";
      for (int i : inputList) {
         word += new Position(i).getLetter();
      }
      return word;
   }
   
   private void visit(Position p) {
      visited[p.x][p.y] = true;
   }
   
   private class Position {
      private int x;
      private int y;
      private int index;
      private String letter;
      private static final int MAX_NEIGHBORS = 8;
      
      Position(int indexIn) {
         this.index = indexIn;
         if (index == 0) {
            this.x = 0;
            this.y = 0;
         }
         else {
            this.x = index % squareSize;
            this.y = index / squareSize;
         }
         this.letter = board[y][x];
      }
      
      Position (int xIn, int yIn) {
         this.x = xIn;
         this.y = yIn;
         this.index = (y * squareSize) + x;
         this.letter = board[y][x];
      }
      
      public Position[] neighbors(LinkedList<Integer> visited) {
         Position[] adj = new Position[maxNeighbors];
         int count = 0;
        
         for (int i = this.x -1; i <= this.x + 1; i++) {
            for (int k = this.y - 1; k <= this.y + 1; k++) {
               if (!((i == this.x) && (k== this.y))) {
                  if (isValid(i, k) && !visited.contains((k * squareSize) + i)) {
                     Position p = new Position (i, k);
                     adj[count++] = p;
                  }
               }
            }
         }
         return adj;
      
      }
   
   
      public String getLetter() {
         return letter;
      }
   
      public int getIndex() {
         return index;
      }
   }
   
   


}