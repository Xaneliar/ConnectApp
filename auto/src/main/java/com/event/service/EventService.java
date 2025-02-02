package com.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.event.repo.EventMongoRepository;

@Service
public class EventService {

	

	@Autowired
	private EventMongoRepository repository;


	@Autowired
	private MongoOperations mongo;
	
	public void deleteAll() {
		repository.deleteAll();
	}
	

	

}
