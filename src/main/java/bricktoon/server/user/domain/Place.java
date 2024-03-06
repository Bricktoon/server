package bricktoon.server.user.domain;

import bricktoon.server.global.response.BaseException;
import bricktoon.server.global.response.BaseResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Place {
    ANSAN("안산점"),
    HYEHWA("혜화점");

    private final String key;

    public static Place findByKey(String key) {
        for (Place place : Place.values()) {
            if (place.getKey().equals(key))
                return place;
        }
        throw new BaseException(BaseResponseStatus.NOT_FOUND_PLACE);
    }
}
