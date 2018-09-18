package com.kema.management.web;

import com.kema.management.entities.NodeInfo;
import com.kema.management.repository.NodeInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class NodeinfoApi {

    static final Logger logger = LoggerFactory.getLogger(NodeinfoApi.class);

    @Autowired
    NodeInfoRepository repository;

    @RequestMapping("/node/instances")
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public List<NodeInfo> list()
    {
        logger.info("get all instances...");
        List<NodeInfo> result = repository.findAll();
        logger.info("{} instances found.", result.size());
        return result;
    }

    @RequestMapping(value = "/node/instances/{id}", method = RequestMethod.GET)
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    public ResponseEntity<NodeInfo> getInstance(@PathVariable("id") Long id) throws IOException {
        logger.info("Querying instance ...");
        NodeInfo result = repository.getOne(id);

        if(result != null){
            return new ResponseEntity<NodeInfo>(result, HttpStatus.OK);
        }
        return new ResponseEntity<NodeInfo>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/node/instances", method = RequestMethod.POST)
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, readOnly = false)
    public NodeInfo addInstance(@RequestBody NodeInfo info) throws IOException {
        logger.info("Saving instance ...");
        final NodeInfo result = repository.save(info);
        logger.info("Instance ({}) saved.", result.getId());
        return result;
    }

    @RequestMapping(value = "/node/instances/{id}", method = RequestMethod.DELETE)
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED, readOnly = false)
    public String deleteInstance(@PathVariable("id") Long id) throws IOException {
        logger.info("Querying instance ...");
        NodeInfo result = repository.getOne(id);

        if(result != null){
            logger.info("Deleting instance ...");
            repository.delete(result);
            logger.info("Instance ({}) deleted.", result.getId());
        }
        return "Node : " + result.getId() + " delted";
    }

}
