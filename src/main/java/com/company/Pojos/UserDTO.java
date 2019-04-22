package com.company.Pojos;

import com.vk.api.sdk.queries.users.UsersSearchRelation;
import com.vk.api.sdk.queries.users.UsersSearchSex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private Integer ageFrom;
    private Integer ageTo;
    private UsersSearchSex sex;
    private List<UsersSearchRelation> relationList;
    private Boolean hasPhoto;
    private Integer cityCode;
    private String date;
    private Calendar lastSeen = new GregorianCalendar(2019, Calendar.MARCH, 1);
    private Map<Integer, Double> groupsMap;
    private Map<String, Double> booksMap;

    public UserDTO() {
        this.userId = 0;
        this.ageFrom = 0;
        this.ageTo = 0;
        this.sex = UsersSearchSex.FEMALE;

        this.relationList = Arrays.asList(UsersSearchRelation.SINGLE, UsersSearchRelation.ACTIVELY_SEARCHING);
        this.lastSeen = new GregorianCalendar(2019, Calendar.MARCH, 1);
        this.hasPhoto = false;
        this.cityCode = 0;
        this.groupsMap = new HashMap<>();
        this.groupsMap.put(0, 0.0);
        this.groupsMap.put(1, 0.0);

        this.booksMap = new HashMap<>();
        this.booksMap.put("Жюль Верн", 1.0);
        this.booksMap.put("Азимов", 1.1);
    }

}
