package ru.yandex.practicum.filmorate.dao;

public interface LikesDao {
    void addLikes(long filmId, long userId);
    void removeLikes(long filmId, long userId);
}
