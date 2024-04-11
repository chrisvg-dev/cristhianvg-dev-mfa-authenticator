package com.cristhianvg.MFAGoogleAuthenticator;

import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUserRole;
import com.cristhianvg.MFAGoogleAuthenticator.repository.IRoleRepository;
import com.cristhianvg.MFAGoogleAuthenticator.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class MfaGoogleAuthenticatorApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(MfaGoogleAuthenticatorApplication.class, args);
	}

	@Autowired private IRoleRepository roleRepository;
	@Override
	public void run(String... args) throws Exception {
		/*this.roleRepository.saveAll(List.of(
			CustomUserRole.builder().roleName(CustomUserRole.ERole.USER).build(),
			CustomUserRole.builder().roleName(CustomUserRole.ERole.ADMIN).build(),
			CustomUserRole.builder().roleName(CustomUserRole.ERole.WRITE).build(),
			CustomUserRole.builder().roleName(CustomUserRole.ERole.READ).build()
		));*/
	}
}
