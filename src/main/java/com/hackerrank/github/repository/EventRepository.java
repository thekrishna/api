package com.hackerrank.github.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hackerrank.github.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event,Long>{
	Optional<Event> findById(Long id);
	List<Event> findAllOrderById();
}
