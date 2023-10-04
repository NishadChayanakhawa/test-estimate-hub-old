package io.github.nishadchayanakhawa.testestimatehub.model.dto.records;

//import section
//java-time
import java.time.LocalDate;
//jackson json
import com.fasterxml.jackson.annotation.JsonFormat;
//lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: ReleaseDTO<br>
 * <b>Description</b>: DTO representation for Release record.<br>
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseDTO {
	// id
	private Long id;
	// identifier
	private String identifier;
	// name
	private String name;
	// start date
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	// end date
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	
	public ReleaseDTO(String identifier,String name,LocalDate startDate,LocalDate endDate) {
		this(null,identifier,name,startDate,endDate);
	}
	
	public ReleaseDTO(Long id) {
		this(id,null,null,null,null);
	}
}
