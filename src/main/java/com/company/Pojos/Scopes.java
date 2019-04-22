package com.company.Pojos;

/**Права доступа для токена пользователя*/
public enum Scopes {
    notify(1,  	"Пользователь разрешил отправлять ему уведомления (для flash/iframe-приложений)."),
    friends(2, "Доступ к друзьям."),
    photos(4, "Доступ к фотографиям."),
    audio(8, "Доступ к аудиозаписям."),
    video(16, "Доступ к видеозаписям."),
    stories(64, "Доступ к историям."),
    pages(128, "Доступ к wiki-страницам."),
    status(1024, "Доступ к статусу пользователя."),
    notes(2048, "Доступ к заметкам пользователя."),
    messages(4096, "Доступ к расширенным методам работы с сообщениями (только для Standalone-приложений, " +
            "прошедших модерацию)."),
    wall(8192, "Доступ к обычным и расширенным методам работы со стеной.\n" +
            "Данное право доступа по умолчанию недоступно для сайтов (игнорируется при попытке авторизации " +
            "для приложений с типом «Веб-сайт» или по схеме Authorization Code Flow)."),
    ads(32768, "Доступ к расширенным методам работы с рекламным API. Доступно для авторизации " +
            "по схеме Implicit Flow или Authorization Code Flow."),
    offline(65536, "Доступ к API в любое время (при использовании этой опции параметр expires_in, " +
            "возвращаемый вместе с access_token, содержит 0 — токен бессрочный). Не применяется в Open API."),
    docs(131072, "Доступ к документам."),
    groups(262144, "Доступ к группам пользователя."),
    notifications(524288, "Доступ к оповещениям об ответах пользователю."),
    stats(1048576, "Доступ к статистике групп и приложений пользователя, администратором которых он является."),
    email(4194304, "Доступ к email пользователя."),
    market(134217728, "Доступ к товарам. ");

    int code;
    String description;

    Scopes(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Long summarizeScope(Scopes... scopes){
        Long result = 0L;
        for (Scopes element : scopes) {
            result += element.code;
        }
        return result == 0L ? null : result;
    }
}
