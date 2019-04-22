package com.company.services;

import com.company.Pojos.Scopes;
import com.company.Pojos.UserInfo;
import com.company.Pojos.Util;
import SearchPeopleThroughVk.tables.pojos.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.company.repository.AuthRepository;

import java.io.*;

@Service
public class AuthService {

    @Value("${version.api.vk}")
    String versionApiVk;

    @Value("${application.id}")
    String applicationId;

    @Value("${file.serializable}")
    String filePath;

    @Autowired
    AuthRepository authRepository;

    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    /**
     * Выдаем пользователю ссылку для авторизации, с полномочиями доступа к группам, фоткам, и аудио
     * @return Ссылка авторизации, ктр необходимо ввести
     */
    public String getUrlForAuth(){
        Long summScopes = Scopes.summarizeScope(Scopes.audio, Scopes.groups, Scopes.photos);
        return Util.getUrlAuthorize(Long.parseLong(applicationId), summScopes, Double.parseDouble(versionApiVk));
    }
    
    public void addToken(String rawToken){
        UserInfo userInfo = extractingUserInfo(rawToken);
        serializingUserInfo(userInfo);
    }

//    public UserInfo fetchByUserId(Integer userId){
//        Customer customer = authRepository.fetchByCustomerByUserId(userId);
//        return new UserInfo(customer.getUserId().intValue(), customer.getAccessKey());
//    }

    private UserInfo extractingUserInfo(String rawString){
        UserInfo userInfo = new UserInfo();
        String [] requestParams;
        if(rawString != null) {
            requestParams = rawString.split("&");
            for (String s: requestParams) {
                if(s.contains("access_token=")){
                    int bodyKeyIndex = s.indexOf("access_token=") + "access_token=".length();
                    userInfo.setToken(s.substring(bodyKeyIndex));
                }
                if(s.contains("user_id=")){
                    int bodyKeyIndex = s.indexOf("user_id=") + "user_id=".length();
                    userInfo.setUserId(Integer.parseInt(s.substring(bodyKeyIndex).trim()));
                }
            }
        }
        return !userInfo.getToken().isEmpty() && !(userInfo.getUserId() == null) ? userInfo : null;
    }

    private String requestToVk(String url){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");

        HttpEntity entity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return responseEntity.getBody();
    }

    public void serializingUserInfo(UserInfo userInfo) {
        try {
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(userInfo);
            out.close();
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public UserInfo deserializingUserInfo() {
        UserInfo userInfo = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fis);
            userInfo = (UserInfo)in.readObject();
            in.close();
            fis.close();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
