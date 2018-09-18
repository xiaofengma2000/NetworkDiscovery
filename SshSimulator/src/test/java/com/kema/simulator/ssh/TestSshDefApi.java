package com.kema.simulator.ssh;

import com.kema.simulator.ssh.common.SshDef;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TestSshDefApi {

    @Test
    public void testList() {
        List<SshDef> insList = getSshDefs();
        Assert.assertTrue(insList.size() > 0);
    }

    private List<SshDef> getSshDefs() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<SshDef>> response = restTemplate.exchange(
                "http://localhost:8085/ssh/instances",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SshDef>>() {
                });
        return response.getBody();
    }

    @Test
    public void testCreate() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        SshDef sd = new SshDef();
        sd.setPort(24);
        sd.setType("Default");

        final ResponseEntity<SshDef> res = restTemplate.postForEntity("http://localhost:8085/ssh/instances", sd, SshDef.class);

        final SshDef result = res.getBody();
        Assert.assertTrue(result != null);
        Assert.assertEquals(sd.getPort(), result.getPort());
    }

    @Test
    public void testDelete() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        List<SshDef> insList = getSshDefs();
        for(SshDef sd : insList){
            if(24 == sd.getPort()){
                String entityUrl = "http://localhost:8085/ssh/instances/" + sd.getId();
                restTemplate.delete(entityUrl);
            }
        }

    }
}
