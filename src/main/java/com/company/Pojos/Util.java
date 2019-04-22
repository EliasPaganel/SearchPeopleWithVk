package com.company.Pojos;

import java.time.LocalDate;
import java.time.Period;

public class Util {

    public static final String URL_AUTHORIZE = "https://oauth.vk.com/authorize?" +
            "client_id={clientId}&" +
            "display=page&" +
            "redirect_uri=https://oauth.vk.com/blank.html&" +
            "scope={sumScope}&" +
            "response_type=token&" +
            "v={versionApi}";

    public static String getUrlAuthorize(Long clientId, Long summScope, Double versionApi){
        return URL_AUTHORIZE.replace("{clientId}", clientId.toString())
                .replace("{sumScope}", summScope.toString())
                .replace("{versionApi}", versionApi.toString());
    }


    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}
