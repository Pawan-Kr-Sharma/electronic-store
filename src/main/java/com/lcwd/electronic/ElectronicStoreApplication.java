package com.lcwd.electronic;

import com.lcwd.electronic.entities.Role;
import com.lcwd.electronic.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@EnableWebMvc
@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
	//for role id dynamic
	@Value("${admin.role.id}")
	String role_admin_id;
	@Value("${normal.role.id}")
	String role_normal_id;

	//for password encoding bcoz phle hmne normal password use kia tha just for checking (
	//(leave this if you want)
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("pk123")); //only just for testing to password encode

		//role define
		try{
//			if we give dynamic by application properties
//			String role_admin_id="jfkkjdflsdhgldjhdfj";
//			String role_normal_id="jfkkjdflsdhgldjhdffghj";

			Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			roleRepository.save(role_admin);
			roleRepository.save(role_normal);
		}catch (Exception e){
			e.printStackTrace();
		}



	}
}
