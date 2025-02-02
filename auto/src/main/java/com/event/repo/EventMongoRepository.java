package com.event.repo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zerodhatech.models.Trade;


@Repository
public interface EventMongoRepository extends MongoRepository<Trade, Long>  {

	
}