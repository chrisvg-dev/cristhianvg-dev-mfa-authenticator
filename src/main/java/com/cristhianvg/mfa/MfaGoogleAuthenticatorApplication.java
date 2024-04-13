package com.cristhianvg.mfa;

import com.cristhianvg.mfa.entities.UserPermission;
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
				UserPermission.builder().permissionName(EPermission.USERS_DELETE).build(),
				UserPermission.builder().permissionName(EPermission.USERS_READ).build(),
				UserPermission.builder().permissionName(EPermission.USERS_WRITE).build(),
				UserPermission.builder().permissionName(EPermission.USERS_UPDATE).build(),
				UserPermission.builder().permissionName(EPermission.ADMIN).build()
			));
		}
	}
}
