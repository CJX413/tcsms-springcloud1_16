package com.tcsms.securityserver.Service.ServiceImp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.JSON.ResultJSON;
import com.tcsms.securityserver.JSON.SendJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.tcsms.securityserver.Config.ConstantConfig.ERROE_RECEIVE_URL;
import static com.tcsms.securityserver.Config.ConstantConfig.WARNING_RECEIVE_URL;

@Service
public class RestTemplateServiceImp {
    @Autowired
    private RestTemplate restTemplate;

    public String sendJson(String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(json, headers);
        return restTemplate.postForObject(url, formEntity, String.class);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public SendJSON sendWarning(WarningInfo warningInfo, JsonArray data) throws SendWarningFailedException {
        SendJSON json = new SendJSON(warningInfo.getCode(),
                warningInfo.getMsg(), data);
        ResultJSON result = new Gson().fromJson(sendJson(WARNING_RECEIVE_URL, json.toString()),
                ResultJSON.class);
        if (result.getCode() != 200) {
            throw new SendWarningFailedException();
        }
        return json;
    }

    public SendJSON sendException(ExceptionInfo exceptionInfo, JsonArray data) {
        SendJSON json = new SendJSON(exceptionInfo.getCode(),
                exceptionInfo.getMsg(), data);
        ResultJSON result = new Gson().fromJson(sendJson(ERROE_RECEIVE_URL, json.toString()),
                ResultJSON.class);
        return json;
    }
}
