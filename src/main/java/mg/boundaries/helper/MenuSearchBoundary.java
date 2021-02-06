package mg.boundaries.helper;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuSearchBoundary {

	private Date fromDate;
	private Date toDate;
}
