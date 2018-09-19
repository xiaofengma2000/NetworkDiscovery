package com.kema.discovery;

import com.kema.discovery.entities.EntityObj;
import com.kema.discovery.repository.EntityRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ActiveProfiles({ "test", "unit" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DiscoveryMain.class })
public class TestFongo {

    @Autowired
    private EntityRepository repository;

    @Test
    public void testCreate() {
        final EntityObj obj = new EntityObj();
        obj.setId("Test");
        obj.setType("TEST");
        repository.save(obj);
        List<EntityObj> list = repository.findAll();
        assertTrue(list.size() == 1);
        assertEquals("Test", list.get(0).getId());
        assertEquals("TEST", list.get(0).getType());
    }

}
