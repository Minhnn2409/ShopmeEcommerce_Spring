package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandServiceTests {

	@Mock
	private BrandRepository repo;

	@InjectMocks
	private BrandService service;

	@Test
	public void testCheckDuplicateInNewModeReturnDuplicate() {
		Integer id = null;
		String name = "Apple";

		Brand newBrand = new Brand(name);
		Mockito.when(repo.findByName(name)).thenReturn(newBrand);

		String result = service.checkUniqueBrand(id, name);
		assertThat(result).isEqualTo(result);
	}

	@Test
	public void testCheckDuplicateInEditModeReturnDuplicate() {
		Integer id = 2;
		String name = "Apple";

		Brand editBrand = new Brand(1, name);

		Mockito.when(repo.findByName(name)).thenReturn(editBrand);

		String result = service.checkUniqueBrand(id, name);

		assertThat(result).isEqualTo("Duplicate");
	}

	@Test
	public void testCheckDuplicateInNewModeReturnOk() {
		Integer id = null;
		String name = "Apple";

		Brand newBrand = new Brand(name);

		Mockito.when(repo.findByName(name)).thenReturn(newBrand);

		String result = service.checkUniqueBrand(id, "samsung");

		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testCheckDuplicateInEditModeReturnOk() {
		Integer id = 1;
		String name = "Apple";

		Brand editBrand = new Brand(id, name);
		Mockito.when(repo.findByName(name)).thenReturn(editBrand);

		String result = service.checkUniqueBrand(id, name);

		assertThat(result).isEqualTo("OK");
	}
}
