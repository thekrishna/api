package com.hackerrank.github.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.hackerrank.github.model.Actor;

@Repository
public interface ActorRepository extends CrudRepository<Actor,Long>{
}
