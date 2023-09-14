package io.github.nishadchayanakhawa.testestimatehub.configurations;

//import section
//model mapper
import org.modelmapper.ModelMapper;
//spring boot libraries
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <b>Class Name</b>: BeanCollection<br>
 * <b>Description</b>: Holds collection of bean configurations used by application.<br>
 * @author nishad.chayanakhawa
 */
@Component
public class BeanCollection {
	
	/**
	 * <b>Method Name</b>: modelMapper<br>
	 * <b>Description</b>: Model Mapper bean configuration.<br>
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
