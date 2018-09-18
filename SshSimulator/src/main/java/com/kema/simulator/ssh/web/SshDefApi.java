package com.kema.simulator.ssh.web;

import com.kema.simulator.ssh.SshdServer;
import com.kema.simulator.ssh.common.SshDef;
import com.kema.simulator.ssh.repository.SshDefRepository;
import org.apache.sshd.common.Factory;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.PropertyResolverUtils;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.jline.builtins.ssh.Ssh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
public class SshDefApi {

    static final Logger logger = LoggerFactory.getLogger(SshDefApi.class);

    Map<Long, SshdServer> serverMap = new HashMap<>();

    @Autowired
    SshDefRepository repository;

    @RequestMapping("/ssh/instances")
    @Transactional(transactionManager = "sshdTransactionManager", propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public List<SshDef> list()
    {
        logger.info("get all instances...");
        ;
        List<SshDef> result = repository.findAll();
        logger.info("{} instances found.", result.size());
        return result;
    }

    @RequestMapping(value = "/ssh/instances", method = RequestMethod.POST)
    @Transactional(transactionManager = "sshdTransactionManager", propagation = Propagation.REQUIRED, readOnly = false, noRollbackFor = Exception.class)
    public ResponseEntity<SshDef> addInstance(@RequestBody SshDef sshDef) throws IOException {
        logger.info("Starting instance ...");
        final SshdServer server = startInstance(sshDef);
        logger.info("Saving instance ...");
        final SshDef result = repository.save(sshDef);
        logger.info("Instance ({}) saved.", result.getId());
        serverMap.put(result.getId(), server);
        return new ResponseEntity<SshDef>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/ssh/instances/{id}", method = RequestMethod.DELETE)
    @Transactional(transactionManager = "sshdTransactionManager", propagation = Propagation.REQUIRED, readOnly = false, noRollbackFor = Exception.class)
    public ResponseEntity<String> addInstance(@PathVariable("id") Long id) throws IOException {
        logger.info("Starting instance ...");
        SshDef result = repository.getOne(id);

        if(result != null){
            logger.info("Deleting instance ...");
            repository.delete(result);
            logger.info("Instance ({}) saved.", result.getId());
            logger.info("Stopping instance ...");
            serverMap.remove(result.getId()).getSshd().stop();
        }
        return new ResponseEntity<String>("SSHD : " + result.getId() + " delted", HttpStatus.OK);
    }

    @Autowired
    Factory<Command> cmdFactory;

    SshdServer startInstance(SshDef def) throws IOException {

        SshdServer sshdServer = new SshdServer(def.getType(), cmdFactory);

        final long idleTimeoutValue = TimeUnit.SECONDS.toMillis(def.getIdleTimeout());
        PropertyResolverUtils.updateProperty(sshdServer.getSshd(), FactoryManager.IDLE_TIMEOUT, idleTimeoutValue);

        final long disconnectTimeoutValue = TimeUnit.SECONDS.toMillis(def.getDisconnectTimeout());
        PropertyResolverUtils.updateProperty(sshdServer.getSshd(), FactoryManager.DISCONNECT_TIMEOUT, disconnectTimeoutValue);

        sshdServer.getSshd().setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshdServer.getSshd().setPort(def.getPort());
        sshdServer.getSshd().setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return true;
            }
        });
        sshdServer.getSshd().start();
        return sshdServer;
    }

    @PostConstruct
    private void startDefaultSshd() {
        logger.info("Starting the default SSHD");
        SshDef sd = new SshDef();
        sd.setType("Default");
        try{
            addInstance(sd);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
