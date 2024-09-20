package com.solon.airbnb.user.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.solon.airbnb.user.domain.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class UsersSpecification implements Specification<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final User example;

	public UsersSpecification(User example) {
		this.example = example;
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotEmpty(example.getUsername())) {
			predicates.add(
					cb.like(root.get("username"), cb.literal("%" + example.getUsername().toLowerCase() + "%"))
			);
		}
		
		if (StringUtils.isNotEmpty(example.getFirstName())) {
			predicates.add(
					cb.like(
							cb.lower(root.get("firstName")),
							cb.lower(cb.literal("%" + example.getFirstName().toLowerCase() + "%"))
					)
			);
		}
		
		if (StringUtils.isNotEmpty(example.getLastName())) {
			predicates.add(
					cb.like(
							cb.lower(root.get("lastName")), 
							cb.lower(cb.literal("%" + example.getLastName().toLowerCase() + "%"))
					)
			);
		}
		
		if (StringUtils.isNotEmpty(example.getEmail())) {
			predicates.add(
					cb.like(root.get("email"), cb.literal("%" + example.getEmail().toLowerCase() + "%"))
			);
		}
		
		if (example.getStatus() != null) {
			predicates.add(cb.equal(root.get("status"), example.getStatus()));
		}
//		root.fetch("roles");
		return andTogether(predicates, cb);
	}

	private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
		return cb.and(predicates.toArray(new Predicate[0]));
	}

}
