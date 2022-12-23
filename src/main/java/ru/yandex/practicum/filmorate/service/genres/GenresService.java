package ru.yandex.practicum.filmorate.service.genres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenresService {

    private final GenreStorage genreStorage;

    public GenresService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
       }

    public Collection<Genre> getAll() {
        log.info("Обращение в genreStorage");
        return genreStorage.getAll();
    }

    public Genre getById(int id) {
        log.info("Обращение в genreStorage");
        return genreStorage.getById(id);
    }
}
