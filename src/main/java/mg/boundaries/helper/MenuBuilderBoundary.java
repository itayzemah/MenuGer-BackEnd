package mg.boundaries.helper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.boundaries.RecipeBoundary;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MenuBuilderBoundary {
	
	private RecipeBoundary[] recipes;
	private String userEmail;
}
