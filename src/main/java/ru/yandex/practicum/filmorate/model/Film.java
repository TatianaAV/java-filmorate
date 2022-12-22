package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private int rate;//count likes
    private MPA mpa;
    private Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));

    public Film(long filmId, String nameFilm, String description, LocalDate releaseDate, long duration, int rate) {
        this.id = filmId;
        this.name = nameFilm;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    //test
    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(String nameFilm, String description, LocalDate releaseDate, long duration, int rate) {
        this.name = nameFilm;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return name.equals(film.name) && releaseDate.equals(film.releaseDate) && Objects.equals(mpa, film.mpa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, releaseDate, mpa);
    }
}


