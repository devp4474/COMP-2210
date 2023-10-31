import java.util.Arrays;


/**
 * Autocomplete.
 */
public class Autocomplete {

   private Term[] terms;

	/**
	 * Initializes a data structure from the given array of terms.
	 * This method throws a NullPointerException if terms is null.
	 */
   public Autocomplete(Term[] terms) {
      if (terms == null) {
         throw new NullPointerException();
      }
      this.terms = terms;
      Arrays.sort(terms);
   
   }

	/** 
	 * Returns all terms that start with the given prefix, in descending order of weight. 
	 * This method throws a NullPointerException if prefix is null.
	 */
   public Term[] allMatches(String prefix) {
      if (prefix == null) {
         throw new NullPointerException();
      }
      
      int bottom = BinarySearch.<Term>firstIndexOf(terms, new Term(prefix, 0),
         Term.byPrefixOrder(prefix.length()));
      int top = BinarySearch.<Term>lastIndexOf(terms, new Term(prefix, 0),
         Term.byPrefixOrder(prefix.length()));
         
      Term[] same = new Term[top - bottom + 1];
      for (int i = bottom; i < top + 1; i++) {
         same[i - bottom] = terms[i];
      } 
      Arrays.sort(same, Term.byDescendingWeightOrder());
      return same;
   
   }

}

