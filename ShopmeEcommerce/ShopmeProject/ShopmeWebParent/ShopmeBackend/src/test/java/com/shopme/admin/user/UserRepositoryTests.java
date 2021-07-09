package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("minh@codejava.net", "minh2021", "Nguyen", "Ngoc Minh");
		user.addRole(roleAdmin);

		User savedUser = repo.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUserWithTwoRoles() {
		User userRavi = new User("ravi@gmail.com", "ravi2020", "Ravi", "Kumar");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);

		User savedUser = repo.save(userRavi);
		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllUser() {
		Iterable<User> users = repo.findAll();
		users.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User userTest = repo.findById(1).get();
		System.out.println(userTest);
		assertThat(userTest).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		User userUpdate = repo.findById(1).get();
		userUpdate.setEnabled(true);
		userUpdate.setEmail("nnminh24@gmail.com");
		repo.save(userUpdate);

	}

	@Test
	public void testUpdateUserRoles() {
		User updatedUser = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSaleperson = new Role(2);

		updatedUser.getRoles().remove(roleEditor);
		updatedUser.addRole(roleSaleperson);

		repo.save(updatedUser);
	}

	@Test
	public void testDeleteUser() {
		Integer id = 2;
		repo.deleteById(id);
	}

	@Test
	public void testGetUserByEmail() {
		String testEmail = "cuckoo310@gmail.com";
		User user = repo.getUserByEmail(testEmail);
		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testEnableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testListFirstPage() {
		int pageSize = 4;
		int pageNum = 1;

		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> users = page.getContent();
		users.forEach(user -> System.out.println(user));
		assertThat(users.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUser() {
		String keyword = "bruce";
		int pageNum = 0;
		int pageSize = 4;

		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> users = page.getContent();
		users.forEach(user -> System.out.println(user));

		assertThat(users.size()).isGreaterThan(0);
	}
}
