package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class MPA {

    private int id;
    private String name;

    public MPA(int mpaId) {
        this.id=mpaId;
    }

    @Override
    public String toString() {
        return name;
    }

    /* G ("G"),
    PG ("PG"),//12+
    PG13 ("PG-13"),
    R ("R"),
    NC17 ("NC-17")
   ;*/
}

