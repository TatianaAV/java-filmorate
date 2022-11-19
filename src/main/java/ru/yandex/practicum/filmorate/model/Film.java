package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private Set<Long> likes = new HashSet<>();

     public void addLikes(Long idUser) {
        likes.add(idUser);
    }

    public void delLikes(Long idUser) {
        likes.remove(idUser);
    }

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public int getLikeSize() {
        return likes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return name.equals(film.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
