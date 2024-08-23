package org.example.kata;

import org.example.kata.configuration.MyConfig;
import org.example.kata.entity.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        Communication communication = context.getBean("communication", Communication.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers = communication.getSessionId(headers); //подшиваем в заголовок cookie с sessionId
        System.out.println("Cookie после добавления:\n" + headers.get("Cookie"));

        HttpEntity<Object> entity = new HttpEntity<>(new User(3L, "James", "Brown", (byte) 25), headers);

        communication.post(entity);
        entity = new HttpEntity<>(new User(3L, "Thomas", "Shelby", (byte) 25), headers);

        communication.put(entity);

        communication.delete(entity);

        System.out.println("Ответ:\n"+communication.key.toString());
        for (int i = 0; i<communication.key.length(); i++) {
            System.out.print(i + ", ");
        }



        /*String allUsers = communication.getAllUsers();
        System.out.println(allUsers);*/

        /*User userById = communication.showUser(2);
        System.out.println(userById);*/

        /*User user = new User(3L, "James", "Brown", (byte) 14);
        System.out.println(user);*/
    }
}
