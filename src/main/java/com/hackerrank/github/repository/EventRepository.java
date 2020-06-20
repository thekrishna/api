package com.hackerrank.github.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackerrank.github.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>{
	Optional<Event> findById(Long id);
}
