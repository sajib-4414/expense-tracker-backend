package com.sajib_4414.expense.tracker;

import com.sajib_4414.expense.tracker.models.user.Role;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import com.sajib_4414.expense.tracker.models.user.RoleRepository;

@SpringBootApplication
public class ExpenseTrackerApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (!roleRepository.findByName("ROLE_USER").isPresent()) {

			roleRepository.save(new Role(0,"ROLE_USER"));
		}

		if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {

			roleRepository.save(new Role(0,"ROLE_ADMIN"));
		}
	}
}
