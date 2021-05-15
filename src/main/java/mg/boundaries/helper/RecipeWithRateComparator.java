/**
 * 
 */
package mg.boundaries.helper;

import java.util.Comparator;


/**
 * @author itayz
 *
 */
public class RecipeWithRateComparator implements Comparator<RecipeWithRate> {
	@Override
	public int compare(RecipeWithRate r1, RecipeWithRate r2) {
		return Double.compare(r1.getRate(), r2.getRate());

	}
}
