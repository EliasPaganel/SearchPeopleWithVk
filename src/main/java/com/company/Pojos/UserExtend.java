package com.company.Pojos;

import com.vk.api.sdk.objects.users.UserFull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserExtend {
    private Integer id;
    private String firstName;
    private String lastName;
    private String photo;
    private String screenName;
    private String books;
    private Double scores;
    private List<Integer> groupsId = new ArrayList<>();

    public UserExtend(Double scores, Integer groupId, UserFull userFull) {
        this.scores = scores;
        this.id = userFull.getId();
        this.firstName = userFull.getFirstName();
        this.lastName = userFull.getLastName();
        this.photo = userFull.getPhoto200Orig();
        this.screenName = userFull.getScreenName();
        this.books = userFull.getBooks();
        this.groupsId.add(groupId);
    }



}

