package io.github.nishadchayanakhawa.testestimatehub.model;

//import section
//jpa libraries
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
//validation libraries
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
//lombok libraries
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: ApplicationConfiguration<br>
 * <b>Description</b>: Application configuration entity.<br>
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name="TEH_APPLICATION_CONFIGURATION")
@Getter @Setter @NoArgsConstructor
public class ApplicationConfiguration {
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	
	//dummy configuration key to validate case-insensitive uniqueness of 
	//application, module and sub-module
	@Column(nullable=false,length=107,unique=true)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String configurationKey;
	
	//application name
	@Column(nullable=false,length=35)
	@NotBlank(message="Application is required")
	@Size(max=25,message="{maxLength35}")
	private String application;

	//module name
	@Column(nullable=false,length=35)
	@NotBlank(message="Module is required")
	@Size(max=25,message="{maxLength35}")
	private String module;
	
	//sub-module name
	@Column(nullable=false,length=35)
	@NotBlank(message="Sub-Module is required")
	@Size(max=25,message="{maxLength35}")
	private String subModule;
	
	//base test script count
	@Column(nullable=false)
	@NotNull(message="Base test script count is required")
	@Min(value=1,message="Base test script count cannot be lower than 1.")
	private int baseTestScriptCount;
	
	//complexity
	@Column(nullable=false)
	@NotNull(message="Complexity is required")
	@Enumerated(EnumType.ORDINAL)
	private Complexity complexity;
	
	/**
	 * <b>Method Name</b>: prepare<br>
	 * <b>Description</b>: before inserting or updating record, populate configurationKey. 
	 * configurationKey is dummy key to enforce case-insensitive unique constraing on 
	 * application + module + subModule combination. Spaces in individual properties are also 
	 * removed.<br>
	 */
	@PrePersist @PreUpdate
	private void prepare(){
		//remove spaces from application, module and sub-module and concatenate them, separated by ':'
		//assign concatenated value to configurationKey
        this.configurationKey=
        		String.format("%s:%s:%s", 
        				this.application.toLowerCase().replace(" ", ""),
        				this.module.toLowerCase().replace(" ", ""),
        				this.subModule.toLowerCase().replace(" ", ""));
    }
}
