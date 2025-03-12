package by.lapil.audioplayer.utils;

public enum Genres {
    UNKNOWN,
    ROCK,
    POP,
    JAZZ,
    HIPHOP,
    ELECTRONIC,
    BLUES,
    REGGAE,
    COUNTRY,
    METAL,
    CLASSICAL;

    public static Genres parseGenre(String genreStr) {
        try {
            return Genres.valueOf(genreStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
