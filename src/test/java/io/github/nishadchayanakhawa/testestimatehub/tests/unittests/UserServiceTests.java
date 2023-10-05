package io.github.nishadchayanakhawa.testestimatehub.tests.unittests;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import java.util.List;
import io.github.nishadchayanakhawa.testestimatehub.TestEstimateHubApplication;
import io.github.nishadchayanakhawa.testestimatehub.configurations.CommandLineAppStartupRunner;
import io.github.nishadchayanakhawa.testestimatehub.model.configurations.User;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.UserDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.configurations.UserService;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.github.nishadchayanakhawa.testestimatehub.tests.Constants;

/**
 * <b>Class Name</b>: UserServiceTests<br>
 * <b>Description</b>: User service tests<br>
 * @author nishad.chayanakhawa
 */
@SpringBootTest(classes = TestEstimateHubApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserServiceTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserService userService;

	private UserDTO addUser(UserDTO userToAdd) {
		UserDTO addedUser = this.userService.save(userToAdd);
		TestFactory.recordTestStep(String.format("User added: %s", addedUser));
		return addedUser;
	}

	private UserDTO addUser(String scenarioName) {
		return this.addUser(CommandLineAppStartupRunner.getTestUsers().get(scenarioName));
	}

	private void deleteUser(Long id) {
		this.userService.delete(new UserDTO(id));
	}

	// add user
	@Test(groups = { "unit-test" })
	public void addUser_test() {
		TestFactory.recordTest("Add user");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("AddTest");
		UserDTO addedUser = this.addUser(userToAdd);

		// validate added user
		Assertions.assertThat(addedUser.getId()).isPositive();
		Assertions.assertThat(addedUser).usingRecursiveComparison().ignoringFields("id", "password", "roles")
				.isEqualTo(userToAdd);
		Assertions.assertThat(addedUser.getRoles()).containsExactlyInAnyOrderElementsOf(userToAdd.getRoles());

		// delete user
		this.deleteUser(addedUser.getId());
	}

	// modify user
	@Test(groups = { "unit-test" })
	public void updateUser_test() {
		TestFactory.recordTest("Update user");

		UserDTO userToUpdate = this.addUser("UpdateTest");
		UserDTO updatedUser = CommandLineAppStartupRunner.getTestUsers().get("UpdateTestUpdatedUser");
		updatedUser.setId(userToUpdate.getId());

		// update user
		UserDTO updateSavedUser = this.userService.save(updatedUser);
		TestFactory.recordTestStep(String.format("User updated: %s", updateSavedUser));

		// validate modified user
		Assertions.assertThat(updateSavedUser).usingRecursiveComparison().ignoringFields("password", "roles")
				.isEqualTo(updatedUser);
		Assertions.assertThat(updateSavedUser.getRoles()).containsExactlyInAnyOrderElementsOf(updatedUser.getRoles());

		// delete user
		this.deleteUser(updateSavedUser.getId());
	}

	// modify user with no password
	@Test(groups = { "unit-test" })
	public void updateUserNoPassword_test() {
		TestFactory.recordTest("Update user with no password update");

		UserDTO userToUpdate = this.addUser("UpdateNoPasswordTest");
		userToUpdate.setPassword(null);
		userToUpdate.setFirstName("noPassUpdate");

		// update user
		UserDTO updateSavedUser = this.userService.save(userToUpdate);
		TestFactory.recordTestStep(String.format("User updated: %s", updateSavedUser));

		// validate modified user
		Assertions.assertThat(updateSavedUser).usingRecursiveComparison().ignoringFields("password", "roles")
				.isEqualTo(userToUpdate);

		// delete user
		this.deleteUser(updateSavedUser.getId());
	}

	// get user
	@Test(groups = { "unit-test" })
	public void getUser_test() {
		TestFactory.recordTest("Get User Configuration");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("GetTest");
		UserDTO addedUser = this.addUser(userToAdd);

		// get user
		UserDTO retrievedUser = this.userService.get(addedUser.getId());

		// validate
		Assertions.assertThat(retrievedUser).usingRecursiveComparison().isEqualTo(retrievedUser);

		this.deleteUser(retrievedUser.getId());
	}

	// delete user
	@Test(groups = { "unit-test" })
	public void deleteUser_test() {
		TestFactory.recordTest("Delete User Configuration");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("DeleteTest");
		UserDTO addedUser = this.addUser(userToAdd);

		User user = this.userService.loadUserByUsername(addedUser.getUsername());
		Assertions.assertThat(user.isAccountNonExpired()).isTrue();
		Assertions.assertThat(user.isAccountNonLocked()).isTrue();
		Assertions.assertThat(user.isCredentialsNonExpired()).isTrue();
		Assertions.assertThat(user.isEnabled()).isTrue();
		Assertions.assertThat(user.getAuthorities()).hasSize(2);

		// delete user
		this.deleteUser(addedUser.getId());

		// validate
		Assertions.assertThatThrownBy(() -> this.userService.get(addedUser.getId()))
				.isExactlyInstanceOf(EntityNotFoundException.class)
				.hasMessage(String.format("%s with id %d doesn't exist.", "User", addedUser.getId()));

		Assertions.assertThat(this.userService.exists(addedUser.getId())).isFalse();
		Assertions.assertThat(this.userService.loadUserByUsername(addedUser.getUsername())).isNull();
	}

	// get user
	@Test(groups = { "unit-test" })
	public void getAllUsers_test() {
		TestFactory.recordTest("Get all User Configurations");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("GetAllTest");
		UserDTO addedUser = this.addUser(userToAdd);

		// get all user
		List<UserDTO> users = this.userService.getAll();

		// validate
		Assertions.assertThat(users).hasSizeGreaterThan(0);

		this.deleteUser(addedUser.getId());
	}

	// add duplicate username record
	@Test(groups = { "unit-test" })
	public void addUser_duplicateUsername_test() {
		TestFactory.recordTest("Add user with duplicate username");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("AddDuplicateUsername");
		Assertions.assertThatThrownBy(() -> this.addUser(userToAdd)).isExactlyInstanceOf(DuplicateEntityException.class)
				.hasMessage(String.format("%s with %s as '%s' already exists.", "User", "username",
						userToAdd.getUsername()));
	}

	// add duplicate email record
	@Test(groups = { "unit-test" })
	public void addUser_duplicateEmail_test() {
		TestFactory.recordTest("Add user with duplicate email");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("AddDuplicateEmail");
		Assertions.assertThatThrownBy(() -> this.addUser(userToAdd)).isExactlyInstanceOf(DuplicateEntityException.class)
				.hasMessage(String.format("%s with %s as '%s' already exists.", "User", "email", userToAdd.getEmail()));
	}

	// add empty record
	@Test(groups = { "unit-test" })
	public void addUser_emptyRecord_test() {
		TestFactory.recordTest("Add user with empty fields");
		// Add user
		UserDTO userToAdd = CommandLineAppStartupRunner.getTestUsers().get("AddEmpty");
		userToAdd.setUsername("");

		TestFactory.recordTestStep(String.format("Adding user: %s", userToAdd));
		Assertions.assertThatThrownBy(() -> this.addUser(userToAdd)).isExactlyInstanceOf(TransactionException.class)
				.hasMessageContaining(Constants.getRequiredFieldErrorMessage("username"),
						Constants.getRequiredFieldErrorMessage("firstName"),
						Constants.getRequiredFieldErrorMessage("lastName"),
						Constants.getRequiredFieldErrorMessage("email"),
						Constants.getRequiredFieldErrorMessage("roles"));
	}
}
