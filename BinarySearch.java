import java.util.Arrays;
import java.util.Comparator;

/**
 * Binary search.
 */
public class BinarySearch {

    /**
     * Returns the index of the first key in a[] that equals the search key, 
     * or -1 if no such key exists. This method throws a NullPointerException
     * if any parameter is null.
     */
   public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
      if (a == null || key == null || comparator == null) {
         throw new NullPointerException();
      }
      int start = 0, end = a.length - 1, index = -1;
      while (start <= end) {
         int middle = (start + end) / 2;
         Key current = a[middle];
         int comparison = comparator.compare(key, current);
         if (comparison == 0) {
            index = middle;
         }
         if (comparison <= 0) {
            end = middle - 1;
         } 
         else {
            start = middle + 1;
         }
            
      }
      return index;
   }

    /**
     * Returns the index of the last key in a[] that equals the search key, 
     * or -1 if no such key exists. This method throws a NullPointerException
     * if any parameter is null.
     */
   public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
      if (a == null || key == null || comparator == null) {
         throw new NullPointerException();
      }
      int start = 0, end = a.length - 1, index = -1;
      while (start <= end) {
         int middle = (start + end) / 2;
         Key current = a[middle];
         int comparison = comparator.compare(key, current);
         if (comparison == 0) {
            index = middle;
         }
         if (comparison < 0) {
            end = middle - 1;
         } 
         else {
            start = middle + 1;
         }
            
      }
      return index;
      
      
   }

}
