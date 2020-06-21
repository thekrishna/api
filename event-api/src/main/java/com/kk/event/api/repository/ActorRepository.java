package com.kk.event.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kk.event.api.model.Actor;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {
	Optional<Actor> findById(Long id);
}
