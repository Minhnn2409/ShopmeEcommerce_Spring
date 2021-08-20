package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	@Autowired
	private BrandRepository repo;

	@Test
	public void testCreateBrand1() {
		Category laptops = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);

		Brand savedBrand = repo.save(acer);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand2() {
		Brand samsung = new Brand("samsung");

		samsung.getCategories().add(new Category(29));
		samsung.getCategories().add(new Category(24));

		Brand savedBrand = repo.save(samsung);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreate3() {
		Brand apple = new Brand("Apple");

		apple.getCategories().add(new Category(4));
		apple.getCategories().add(new Category(7));

		Brand savedBrand = repo.save(apple);

		assertThat(apple).isNotNull();
		assertThat(apple.getId()).isGreaterThan(0);
	}

	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repo.findAll();
		brands.forEach(System.out::println);

		assertThat(brands).isNotEmpty();
	}

	@Test
	public void testGetById() {
		Brand brand = repo.findById(1).get();

		assertThat(brand.getName()).isEqualTo("Acer");
	}

	@Test
	public void testUpdateBrand() {
		String newName = "Samsung Electronics";
		Brand brand = repo.findById(3).get();

		brand.setName(newName);

		Brand updatedBrand = repo.save(brand);
		assertThat(updatedBrand.getName()).isEqualTo(newName);
	}

	@Test
	public void testDeleteBrand() {
		repo.deleteById(3);

		Optional<Brand> deletedBrand = repo.findById(3);

		assertThat(deletedBrand.isEmpty());
	}

}
