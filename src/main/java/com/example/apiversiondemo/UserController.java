package com.example.apiversiondemo;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

	@GetMapping("/v1/users/{id}")
	public Map<String, Object> uriV1(@PathVariable String id) {
		return Map.of("id", id, "name", "Alice Smith");
	}

	@GetMapping("/v2/users/{id}")
	public Map<String, Object> uriV2(@PathVariable String id) {
		return Map.of(
				"id", id,
				"firstName", "Alice",
				"lastName", "Smith",
				"status", "ACTIVE");
	}

	// ------------------------------------------------------------------------

	@GetMapping(value = "/headers/users/{id}", headers = "API-Version=1")
	public Map<String, Object> headersV1(
			@PathVariable String id,
			@RequestHeader("API-Version") String apiVersion) {
		System.out.println("Header version number specified: " + apiVersion);

		return Map.of("id", id, "name", "Bob Smith");
	}

	@GetMapping(value = "/headers/users/{id}", headers = "API-Version=2")
	public Map<String, Object> headersV2(
			@PathVariable String id,
			@RequestHeader("API-Version") String apiVersion) {
		System.out.println("Header version number specified: " + apiVersion);
		return Map.of(
				"id", id,
				"firstName", "Bob",
				"lastName", "Smith",
				"status", "ACTIVE");
	}

	// ------------------------------------------------------------------------

	@GetMapping(value = "/media/users/{id}", produces = "application/vnd.demo.v1+json")
	public Map<String, Object> mediaV1(@PathVariable String id) {
		System.out.println("v1 vendor");
		return Map.of("id", id, "name", "Paaa Smith");
	}

	@GetMapping(value = "/media/users/{id}", produces = "application/vnd.demo.v2+json")
	public Map<String, Object> mediaV2(@PathVariable String id) {
		System.out.println("v2 vendor");
		return Map.of(
				"id", id,
				"firstName", "Paas",
				"lastName", "Smith",
				"status", "ACTIVE");
	}
}
