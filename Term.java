import java.util.Comparator;

/**
 * Autocomplete term representing a (query, weight) pair.
 * 
 */
public class Term implements Comparable<Term> {
   private String query;
   private long weight;

   /**
    * Initialize a term with the given query and weight.
    * This method throws a NullPointerException if query is null,
    * and an IllegalArgumentException if weight is negative.
    */
   public Term(String query, long weight) {
      if (query == null) {
         throw new NullPointerException();
      }
      if (weight < 0) {
         throw new IllegalArgumentException();
      }
      this.query = query;
      this.weight = weight;   
   
   }
   
   public String getQuery() {
      return this.query;
   }
   public long getWeight() {
      return this.weight;
   }

   /**
    * Compares the two terms in descending order of weight.
    */
   public static Comparator<Term> byDescendingWeightOrder() {
      return new DescendingWeightOrderComparator();
   }
   
   private static class DescendingWeightOrderComparator implements Comparator<Term> {
      public int compare(Term t1, Term t2) {
         if (t1.getWeight() < t2.getWeight()) {
            return 1;
         }
         if (t1. getWeight() > t2.getWeight()) {
            return -1;
         }
         else {
            return 0;
         }
      }
   }

   /**
    * Compares the two terms in ascending lexicographic order of query,
    * but using only the first length characters of query. This method
    * throws an IllegalArgumentException if length is less than or equal
    * to zero.
    */
   public static Comparator<Term> byPrefixOrder(int length) {
      if (length <= 0) {
         throw new IllegalArgumentException();
      }
      return new byPrefixOrderComparator(length);
   
   }
   
   private static class byPrefixOrderComparator implements Comparator<Term> {
      private int length;
      private byPrefixOrderComparator(int length) {
         this.length = length;
      }
      
      public int compare(Term t1, Term t2) {
         String prefix1, prefix2;
         
         if (t1.query.length() < length) {
            prefix1 = t1.query;
         }
         else {
            prefix1 = t1.query.substring(0, length);
         }
         
         if (t2.query.length() < length) {
            prefix2 = t2.query;
         }
         else {
            prefix2 = t2.query.substring(0, length);
         }
         
         return prefix1.compareTo(prefix2);
      }
   }

   /**
    * Compares this term with the other term in ascending lexicographic order
    * of query.
    */
   @Override
   public int compareTo(Term other) {
      return this.query.compareTo(other.query);
   }

   /**
    * Returns a string representation of this term in the following format:
    * query followed by a tab followed by weight
    */
   @Override
   public String toString(){
      return this.query + "\t" + this.weight;
   
   }

}

