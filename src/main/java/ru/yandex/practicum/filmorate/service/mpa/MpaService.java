package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<MPA> getAll() {
        log.info("Обращение в mpaStorage");
        return mpaStorage.getAll();
    }

    public MPA getById(int id) {
        log.info("Обращение в mpaStorage");
        return mpaStorage.getById(id);
    }
}

