package com.kk.event.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kk.event.api.model.Actor;

@Repository
public interface ActorRepository extends CrudRepository<Actor,Long>{
}
