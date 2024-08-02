package com.seyan.film.activity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
public class ActivityOnFilmId {
    private Long userId;
    private Long filmId;
}
