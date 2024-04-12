package com.cristhianvg.mfa;

import com.cristhianvg.mfa.entities.CustomUserPermission;
import com.cristhianvg.mfa.entities.enums.EPermission;
import com.cristhianvg.mfa.repository.IPermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MfaGoogleAuthenticatorApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(MfaGoogleAuthenticatorApplication.class, args);
	}

	@Autowired private IPermissionDao roleRepository;
	@Override
	public void run(String... args) throws Exception {
		if (this.roleRepository.findAll().isEmpty()) {
			this.roleRepository.saveAll(List.of(
				CustomUserPermission.builder().permissionName(EPermission.USER).build(),
				CustomUserPermission.builder().permissionName(EPermission.ADMIN).build(),
				CustomUserPermission.builder().permissionName(EPermission.WRITE).build(),
				CustomUserPermission.builder().permissionName(EPermission.READ).build()
			));
		}
	}
}
