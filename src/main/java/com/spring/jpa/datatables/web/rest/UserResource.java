package com.spring.jpa.datatables.web.rest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jpa.datatables.domain.DataTable;
import com.spring.jpa.datatables.domain.User;
import com.spring.jpa.datatables.repository.UserRepository;
import com.spring.jpa.datatables.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserResource {

	private final Logger log = LoggerFactory.getLogger(UserResource.class);

	private final UserService userService2;

	@Autowired
	UserRepository userRepository2;

	@GetMapping("/allusers")
	public List<User> findAllUsers() {
		// log.debug("REST request to get a page of Users");
		// log.warn("REST request to get a page of Users");
		// log.error("REST request to get a page of Users");
		log.info("REST request to get a page of Users");
		return userService2.findAllUsers();
	}

	@GetMapping("/alluserslist")
	public ResponseEntity<Page<User>> getPaginatedTutorials(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "userId") String sortBy) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy)); // Import PageRequest and Sort
		// Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));
		Page<User> tutorialsPage = userService2.findAllTutorials(pageable);
		return ResponseEntity.ok(tutorialsPage);

	}

	@GetMapping("/usersrestlist")
	public ResponseEntity<Map<String, Object>> getArticles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
		Pageable paging = PageRequest.of(page, size);
		Page<User> result = userRepository2.findAll(paging);

		String base = request.getRequestURL().toString();

		String next = result.hasNext() ? base + "?page=" + (page + 1) + "&size=" + size : null;

		String prev = result.hasPrevious() ? base + "?page=" + (page - 1) + "&size=" + size : null;

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("data", result.getContent());

		Map<String, Object> meta = new LinkedHashMap<>();
		meta.put("currentPage", page);
		meta.put("totalPages", result.getTotalPages());
		meta.put("pageSize", size);
		meta.put("totalItems", result.getTotalElements());
		meta.put("next", next);
		meta.put("previous", prev);
		meta.put("draw", 0);
		meta.put("recordsTotal", result.getTotalElements());
		meta.put("recordsFiltered", result.getTotalElements());

		response.put("pagination", meta);

		return ResponseEntity.ok(response);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/usersrestlisttwo")
	public ResponseEntity listAllTable(@RequestParam("draw") int draw, @RequestParam("start") int start,
			@RequestParam("length") int length, @RequestParam("search[value]") String searchValue) {

		int page = start / length; // Calculate page number

		// Create a specification for the global search
		Specification<User> spec = buildSearchSpecification(searchValue);

		Pageable pageable = PageRequest.of(page, length);

		Page<User> responseData = userRepository2.findAll(spec, pageable);

		DataTable dataTable = new DataTable();

		dataTable.setData(responseData.getContent());
		dataTable.setRecordsTotal(responseData.getTotalElements());
		dataTable.setRecordsFiltered(responseData.getTotalElements());

		dataTable.setDraw(draw);
		dataTable.setStart(start);

		return ResponseEntity.ok(dataTable);

	}

	private Specification<User> buildSearchSpecification(String searchValue) {
	        if (searchValue == null || searchValue.isEmpty()) {
	            return Specification.where(null);
	        }
	        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
	            criteriaBuilder.like(root.get("firstName"), "%" + searchValue + "%"),
	            criteriaBuilder.like(root.get("lastName"), "%" + searchValue + "%"),
	            criteriaBuilder.like(root.get("email"), "%" + searchValue + "%"),
	            criteriaBuilder.like(root.get("username"), "%" + searchValue + "%")
	        );
	    }

}
