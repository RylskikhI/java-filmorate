package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipDao {

    void create(long activeUserId, long passiveUserId);
    boolean isAccepted(long activeUserId, long passiveUserId);
    void update(long activeUserId, long passiveUserId, boolean isAccepted);
    void remove(long activeUserId, long passiveUserId);
    Friendship get(long activeUserId, long passiveUserId);
    List<Friendship> getByUserId(long userId);

}
