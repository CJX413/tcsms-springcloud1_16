package com.tcsms.securityserver.Service.ReceiveServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateServiceImp {
    @Autowired
    private RestTemplate restTemplate;

    public String sendJson(String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
        return restTemplate.postForObject(url, formEntity, String.class);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
