package com.company.services;

import com.company.Pojos.UserDTO;
import com.company.Pojos.UserExtend;
import com.company.Pojos.UserInfo;
import com.company.Pojos.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.actions.Users;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import com.vk.api.sdk.queries.users.UserField;
import com.vk.api.sdk.queries.users.UsersSearchQuery;
import com.vk.api.sdk.queries.users.UsersSearchSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MainService {

    @Autowired
    AuthService authService;

    @Autowired
    TransportClient transportClient;

    @Autowired
    ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(MainService.class);


    public String search(UserDTO userDTO){

        UserInfo userInfo = authService.deserializingUserInfo();

        UserActor userActor;
        if(userInfo != null) {
            userActor = new UserActor(userInfo.getUserId(), userInfo.getToken());
        } else {
            throw new RuntimeException("UserInfo not found");
        }

        VkApiClient vk = new VkApiClient(transportClient);

        Map<Integer, UserExtend> users = new HashMap<>(); //IdUser - Full information about user

        for (Map.Entry<Integer, Double> group: userDTO.getGroupsMap().entrySet()) {

            SearchResponse response = searchPeople(vk, userActor, userDTO, group.getKey());
            logger.info("В группе: {}.Найдено фактически людей:{}. Но count={}", group.getKey(), response.getItems().size(), response.getCount());
            response.getItems().stream()
                    .filter(userFull -> userFull.getLastSeen() != null
                            && userFull.getLastSeen().getTime() != null
                            && (TimeUnit.SECONDS.toMillis(userFull.getLastSeen().getTime()) >= userDTO.getLastSeen().getTimeInMillis()))
                    .filter(userFull -> userFull.getRelation().equals(0) ||
                            userFull.getRelation().equals(1) ||
                            userFull.getRelation().equals(6))
                    .filter(userFull -> {
                        int age = 0;
                        if(userFull.getBdate() != null) {
                            age = Util.calculateAge(LocalDate.parse(userFull.getBdate(), DateTimeFormatter.ofPattern("dd.MM.yyyy")), LocalDate.now());
                        }
                        return userFull.getBdate() == null || userFull.getBdate().length() <= 5 || ((userDTO.getAgeFrom() <= age) && (userDTO.getAgeTo() >= age));
                    })
                    .forEach(userFull -> {

                UserExtend userExtend = users.putIfAbsent(userFull.getId(),
                        new UserExtend(group.getValue(),
                            group.getKey(), userFull));

                if(userExtend != null){
                    UserExtend tempUser = users.get(userFull.getId());
                    if(!tempUser.getGroupsId().contains(group.getKey())) {
                        tempUser.getGroupsId().add(group.getKey());
                        tempUser.setScores(tempUser.getScores() + group.getValue());
                    }
                } else {//добавляю доп баллы за книги, если они есть
                    UserExtend tempUser = users.get(userFull.getId());
                    String books = tempUser.getBooks();
                    if(books != null && !books.isEmpty() && !userDTO.getBooksMap().isEmpty()) {
                        //Накидываю 6 баллов просто за то что раздел book не пустой
                        tempUser.setScores(tempUser.getScores() + 6);
                        for (Map.Entry<String, Double> book: userDTO.getBooksMap().entrySet()) {
                            if(books.contains(book.getKey())) {
                                tempUser.setScores(tempUser.getScores() + book.getValue());
                            }
                        }

                    }
                }
            });

        }

        //Сортируем по убыванию баллов
        List<UserExtend> sorterListUsers = users.values().stream()
                .sorted((o1, o2) -> -o1.getScores().compareTo(o2.getScores()))
                .collect(Collectors.toCollection(ArrayList::new));

        String result;
        try {
            result = objectMapper.writeValueAsString(sorterListUsers);
        } catch (JsonProcessingException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error(errors.toString());
            throw new RuntimeException("При конвертации в строку отсортированного списка пользователей, " +
                    "произошла ошибка");
        }

        return result;
    }

    private SearchResponse searchPeople(
            VkApiClient vk,
            UserActor userActor,
            UserDTO userDTO,
            Integer groupId){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Users users = vk.users();
        UsersSearchQuery searchQuery = users.search(userActor);
        SearchResponse searchResponse;
        try {
            searchResponse = searchQuery
                    .sex(userDTO.getSex())
                    .city(userDTO.getCityCode())
                    .groupId(groupId)
                    .sort(UsersSearchSort.BY_RATING)
                    .hasPhoto(userDTO.getHasPhoto())
                    .count(1000)
                    .fields(UserField.PHOTO_MAX, UserField.SCREEN_NAME,
                            UserField.BOOKS, UserField.LAST_SEEN,
                            UserField.SEX, UserField.RELATION,
                            UserField.BDATE,
                            UserField.CITY, UserField.UNIVERSITIES)
                    .execute();
        } catch (ApiException | ClientException e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error(errors.toString());
            throw new RuntimeException("При запросе поиска, произошла ошибка.");
        }

        return searchResponse;
    }

}
