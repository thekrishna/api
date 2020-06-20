package com.hackerrank.github.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.hackerrank.github.exception.RecordAlreadyExistException;
import com.hackerrank.github.model.Event;
import com.hackerrank.github.repository.EventRepository;

@RestController
public class GithubApiRestController {
	private final EventRepository eventRepository;
	
	@Autowired
	public GithubApiRestController(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	@DeleteMapping("/erase/")
	@ResponseStatus(code = HttpStatus.OK)
	public void eraseEvents() {
		this.eventRepository.deleteAll();
	}
	
	@PostMapping("/events")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Event addEvent(@RequestBody Event event){
		final Optional<Event> optionalEvent = this.eventRepository.findById(event.getId());
		if(optionalEvent.isPresent()) {
			throw new RecordAlreadyExistException(String.format("Event already exist for this % id",event.getId()));
		}
		return this.eventRepository.save(event);
	}
	
	@GetMapping("/events")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEvents(){
		return (List<Event>) this.eventRepository.findAllOrderById();
	}
	
	@GetMapping("/events/repos/{repoID}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Event> getAllEventsByRepoId(@PathVariable Long repoId){
		return (List<Event>) this.eventRepository.findAllOrderById();
	}
	
}
