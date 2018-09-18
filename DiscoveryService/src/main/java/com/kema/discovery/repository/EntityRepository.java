package com.kema.discovery.repository;

import com.kema.discovery.entities.EntityObj;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EntityRepository extends MongoRepository<EntityObj, String>{

    public List<EntityObj> findByType(String type);

}
