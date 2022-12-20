package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class MPA {

    private int id;
    private String name;

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    /* G ("G"),
    PG ("PG"),//12+
    PG13 ("PG-13"),
    R ("R"),
    NC17 ("NC-17")
   ;*/
}

