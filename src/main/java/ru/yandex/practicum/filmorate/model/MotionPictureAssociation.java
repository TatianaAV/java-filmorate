package ru.yandex.practicum.filmorate.model;

public enum MotionPictureAssociation {
    G ("G"),
    PG ("PG"),
    PG13 ("PG-13"),
    R ("R"),
    NC17 ("NC-17")
   ;

    private String title;
    MotionPictureAssociation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}

