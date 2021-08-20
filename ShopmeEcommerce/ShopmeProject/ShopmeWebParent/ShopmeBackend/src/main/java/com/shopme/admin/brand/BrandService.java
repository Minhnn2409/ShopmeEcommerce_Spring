package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {

	public static final int BRANDS_PER_PAGE = 10;

	@Autowired
	private BrandRepository repo;

	public List<Brand> listAll() {
		return (List<Brand>) repo.findAll();
	}

	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);
	}

	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public Brand get(Integer id) {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Could not find any brand with ID " + id);
		}
	}

	public void delete(Integer id) {
		Long countById = repo.countById(id);

		if (countById == 0 || countById == null) {
			throw new NoSuchElementException("Could not find any brand with ID " + id);
		}

		repo.deleteById(id);
	}

	public String checkUniqueBrand(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brand = repo.findByName(name);

		if (isCreatingNew) {
			if (brand != null)
				return "Duplicate";
		} else {
			if (brand != null && brand.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
}
