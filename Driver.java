import java.util.TreeSet;

public class Driver {

   public static void main(String[] args) {
      WordSearchGame game = WordSearchGameFactory.createGame();
      System.out.println(game.getBoard());
      game.loadLexicon("words_medium.txt");
      TreeSet<String> list = new TreeSet<String>(game.getAllScorableWords(8));
      System.out.println(list.size() + "");
   }

}