package io.github.nishadchayanakhawa.testestimatehub.configurations;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.configurations.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Component
@ConfigurationProperties
@PropertySource("classpath:defaultValues.properties")
@Getter
@Setter
@NoArgsConstructor
public class DefaultValues {
	private List<UserDTO> users;

	public List<UserDTO> getDefaultUsers() {
		return users.stream().filter(user -> !(user.getUsername().contains("~"))).toList();
	}

	public Map<String, UserDTO> getTestUsers() {
		Map<String, UserDTO> usersMap = new HashMap<>();
		users.stream().filter(user -> user.getUsername().contains("~")).forEach(user -> {
			String entryKey=user.getUsername().split("~")[0];
			user.setUsername(user.getUsername().split("~")[1]);
			usersMap.put(entryKey, user);
		});
		return usersMap;
	}
}
