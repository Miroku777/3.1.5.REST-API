package org.example.kata;

import org.example.kata.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
//для общения с REST сервисом
public class Communication {
    public StringBuilder key = new StringBuilder();
    private String sessionId;
    private final String URL = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;
    @Autowired
    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @GetMapping()
    public HttpHeaders getSessionId(HttpHeaders headers) {
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);
        sessionId = response.getHeaders().getFirst("set-cookie");
        headers.set("Cookie", sessionId);
        return headers;
    }

    //получить первую фразу
    public void post(HttpEntity<Object> entity) {
        String response = restTemplate.postForObject(URL, entity, String.class);
        key.append(response);
    }

    //получить вторую фразу
    public void put(HttpEntity<Object> entity){
        ResponseEntity<String> response =
                restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
        key.append(response.getBody());
    }

    //получить третью фразу
    public void delete(HttpEntity<Object> entity){
        ResponseEntity<String> response =
                restTemplate.exchange(URL+"/3", HttpMethod.DELETE, entity, String.class);
        key.append(response.getBody());
    }







    public String getAllUsers() {
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        } /*для передачи generic типа*/);
        String allUser = responseEntity.toString();
        return allUser;
    }
    public User showUser(int id) {
        User user = restTemplate.getForObject(URL + "/" + id, User.class);
        return user;
    }
    //если id юзера будет = 0, то метод будет посылать http запрос на создание юзера
    public void saveUser(User user) {
        long id = user.getId();
        if (id == 0) {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, user, String.class);
            System.out.println("New user was added to DB");
            System.out.println(responseEntity.getBody());
        } else {
            restTemplate.put(URL, user);
            System.out.println("User witdh ID " + id + " was updated");
        }
    }
    public void deleteUser(int id) {

    }
}
