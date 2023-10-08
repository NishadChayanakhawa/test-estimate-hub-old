package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//java utils;
import java.util.Set;

import io.github.nishadchayanakhawa.testestimatehub.model.Role;
//lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: UserDTO<br>
 * <b>Description</b>: DTO representation of User entity<br>
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	//id
	private Long id;
	//username
	private String username;
	//password
	private String password;
	//first name
	private String firstName;
	//last name
	private String lastName;
	//email
	private String email;
	//roles
	private Set<Role> roles;
	
	/**
	 * Constructor to initialize UserDTO with only id
	 * @param id as {@link java.lang.Long Long}
	 */
	public UserDTO(Long id) {
		this(id, null, null, null, null, null, null);
	}
}
