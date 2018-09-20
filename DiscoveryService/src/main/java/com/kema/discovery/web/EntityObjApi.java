package com.kema.discovery.web;

import com.kema.discovery.entities.EntityObj;
import com.kema.discovery.repository.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class EntityObjApi {

    @Autowired
    private EntityRepository repository;

    static final Logger logger = LoggerFactory.getLogger(EntityObjApi.class);

    @RequestMapping("/entities")
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public List<EntityObj> list()
    {
        logger.info("get all entities...");
        List<EntityObj> result = repository.findAll();
        logger.info("{} entities found.", result.size());
        return result;
    }

    @RequestMapping(value = "/entities/{id}", method = RequestMethod.GET)
    public ResponseEntity<EntityObj> getEntity(@PathVariable("id") String id) throws IOException {
        logger.info("Querying entity ...");
        EntityObj result = repository.findById(id).get();

        if(result != null){
            return new ResponseEntity<EntityObj>(result, HttpStatus.OK);
        }
        return new ResponseEntity<EntityObj>(HttpStatus.NOT_FOUND);
    }

    @Autowired
    ApplicationContext ctx;

    @PostConstruct
    public void printMongoTemplates(){
        final Set<Map.Entry<String, MongoTemplate>> entries = ctx.getBeansOfType(MongoTemplate.class).entrySet();
        for(Map.Entry<String, MongoTemplate> en : entries){
            System.out.println("Bean name : " + en.getValue() + ", DB name : " + en.getValue().getDb().getName());
        }
//        .stream().forEach(en -> {
//        });
    }

}
