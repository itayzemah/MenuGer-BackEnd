package mg.boundaries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Response<T> {
	private T data;
	private Boolean success = true;
	private String message = "";
	


	
}
