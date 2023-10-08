package io.github.nishadchayanakhawa.testestimatehub.tests.unittests;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.model.User;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.UserService;
import io.github.nishadchayanakhawa.testestimatehub.repositories.UserRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.tests.SpringMockRestApiTestHelper;
import io.github.nishadchayanakhawa.testestimatehub.tests.TestDataProvider;

/**
 * <b>Class Name</b>: UserServiceTests<br>
 * <b>Description</b>: User service tests<br>
 * 
 * @author nishad.chayanakhawa
 */
@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserManagementTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	private static ObjectMapper objectMapper = new ObjectMapper();

	private UserDTO addUser(UserDTO userToAddOrModify) {
		TestFactory.recordTestStep(String.format("User to add/modify: %s", userToAddOrModify));
		UserDTO addedUser = this.userService.save(userToAddOrModify);
		TestFactory.recordTestStep(String.format("User added/modified: %s", addedUser));
		return addedUser;
	}

	private void deleteUser(Long id) {
		this.userService.delete(new UserDTO(id));
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "unit-test" })
	public void addUser_test(JSONObject addUser) throws JsonMappingException, JsonProcessingException {
		TestFactory.recordTest("Add/Get/GetAll/Delete User");
		// Add user
		UserDTO userToAdd = UserManagementTests.objectMapper.readValue(addUser.toString(), UserDTO.class);
		UserDTO addedUser = this.addUser(userToAdd);

		// verify added user
		Assertions.assertThat(addedUser.getId()).isPositive();
		Assertions.assertThat(addedUser).usingRecursiveComparison().ignoringFields("id", "password", "roles")
				.isEqualTo(userToAdd);

		// getUser
		UserDTO retrievedUser = this.userService.get(addedUser.getId());
		Assertions.assertThat(retrievedUser).usingRecursiveComparison().ignoringFields("password", "roles")
				.isEqualTo(addedUser);
		
		//get user
		User user=userRepository.findByUsername(retrievedUser.getUsername());
		Assertions.assertThat(user.getAuthorities()).isNotEmpty();
		Assertions.assertThat(user.isAccountNonExpired()).isTrue();
		Assertions.assertThat(user.isAccountNonLocked()).isTrue();
		Assertions.assertThat(user.isCredentialsNonExpired()).isTrue();
		Assertions.assertThat(user.isEnabled()).isTrue();

		// get all
		List<UserDTO> users = this.userService.getAll();
		Assertions.assertThat(users).isNotEmpty();

		// delete user
		this.deleteUser(addedUser.getId());

		// get deleted user
		Assertions.assertThatThrownBy(() -> this.userService.get(addedUser.getId()))
				.isExactlyInstanceOf(EntityNotFoundException.class)
				.hasMessage(String.format("%s with id %d doesn't exist.", "User", addedUser.getId()));
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "unit-test" })
	public void modifyUser_test(JSONObject addUser)
			throws JsonMappingException, JsonProcessingException, JSONException {
		TestFactory.recordTest("Modify User");
		// add user for modification
		UserDTO userToAdd = UserManagementTests.objectMapper.readValue(addUser.getJSONObject("userToAdd").toString(),
				UserDTO.class);
		UserDTO addedUser = this.addUser(userToAdd);

		// get id of added user
		UserDTO userToModify = UserManagementTests.objectMapper
				.readValue(addUser.getJSONObject("userToModify").toString(), UserDTO.class);
		userToModify.setId(addedUser.getId());

		// modify user
		UserDTO modifiedUser = this.addUser(userToModify);

		// validate modified user
		Assertions.assertThat(modifiedUser).usingRecursiveComparison().ignoringFields("password", "roles")
				.isEqualTo(userToModify);

		// modify user without password change
		userToModify.setPassword(null);
		modifiedUser = this.addUser(userToModify);

		// verify modified user
		Assertions.assertThat(modifiedUser).usingRecursiveComparison().ignoringFields("password", "roles")
				.isEqualTo(userToModify);
		// delete user
		this.deleteUser(addedUser.getId());
	}

	@Test(dataProvider = "getTestDataFromJson", dataProviderClass = TestDataProvider.class, groups = { "unit-test" })
	public void addUser_invalidRequest_test(JSONObject addUser)
			throws JsonMappingException, JsonProcessingException, JSONException {
		TestFactory.recordTest("Add User with invalid requests");
		// add user
		UserDTO userToAdd = UserManagementTests.objectMapper.readValue(addUser.getJSONObject("userToAdd").toString(),
				UserDTO.class);
		UserDTO addedUser = this.addUser(userToAdd);

		// get id of added user
		UserDTO duplicateUsernameToAdd = UserManagementTests.objectMapper
				.readValue(addUser.getJSONObject("duplicateUsernameToAdd").toString(), UserDTO.class);

		// add user with duplicate username and validate error response
		String[] errorDetails = SpringMockRestApiTestHelper
				.toArrayOfStrings(addUser.getJSONArray("expectedErrors_duplicateUsername"));
		Assertions.assertThatThrownBy(() -> this.userService.save(duplicateUsernameToAdd))
				.isExactlyInstanceOf(DuplicateEntityException.class).hasMessageContainingAll(errorDetails);

		// add user with duplicate email and validate error response
		UserDTO duplicateEmailToAdd = UserManagementTests.objectMapper
				.readValue(addUser.getJSONObject("duplicateEmailToAdd").toString(), UserDTO.class);

		errorDetails = SpringMockRestApiTestHelper
				.toArrayOfStrings(addUser.getJSONArray("expectedErrors_duplicateEmail"));
		Assertions.assertThatThrownBy(() -> this.userService.save(duplicateEmailToAdd))
				.isExactlyInstanceOf(DuplicateEntityException.class).hasMessageContainingAll(errorDetails);

		// add user with invalid request and validate error response
		UserDTO invalidUserToAdd = UserManagementTests.objectMapper
				.readValue(addUser.getJSONObject("invalidUserToAdd").toString(), UserDTO.class);
		errorDetails = SpringMockRestApiTestHelper
				.toArrayOfStrings(addUser.getJSONArray("expectedErrors_invalidRequest"));

		Throwable transactionException = Assertions.catchThrowable(() -> this.userService.save(invalidUserToAdd));
		String[] actualErrorDetails = transactionException.getMessage().split(";");

		Assertions.assertThat(transactionException).isExactlyInstanceOf(TransactionException.class);
		Assertions.assertThat(actualErrorDetails).containsExactlyInAnyOrder(errorDetails);

		// delete user
		this.deleteUser(addedUser.getId());
	}
}