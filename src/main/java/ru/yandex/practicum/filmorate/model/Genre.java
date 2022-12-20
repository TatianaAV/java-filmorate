package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    private int id;
    private String name;

    @Override
    public String toString() {
        return " id='" + id + '\'';
    }
}
