package bricktoon.server.book.domain;

import bricktoon.server.global.entity.BaseTimeEntity;
import bricktoon.server.user.domain.Place;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String genre;

    private String location;

    private Integer number;

    private Place place;

    public void update(String name, String genre, String location, Integer number, Place place) {
        this.name = name;
        this.genre = genre;
        this.location = location;
        this.number = number;
        this.place = place;
    }
}
