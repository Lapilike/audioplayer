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
        if (genreStr == null || genreStr.isEmpty()) return null;
        try {
            return Genres.valueOf(genreStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
