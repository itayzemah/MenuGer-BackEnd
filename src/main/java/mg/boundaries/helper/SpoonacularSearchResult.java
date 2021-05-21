package mg.boundaries.helper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SpoonacularSearchResult<T> {

	private int offset;
	private int number;
	private int totalResults;
	private T[] results;
}
