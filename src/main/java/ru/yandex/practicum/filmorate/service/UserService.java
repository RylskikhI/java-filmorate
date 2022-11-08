package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.exception.FriendshipNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserService(UserStorage userStorage, FriendshipDao friendshipDao) {
        this.userStorage = userStorage;
        this.friendshipDao = friendshipDao;
    }

    public void addFriend(long userId1, long userId2) {
        Friendship friendship = friendshipDao.get(userId2, userId1);
        if (friendship == null) {
            friendship = friendshipDao.get(userId1, userId2);
            if (friendship == null) {
                friendshipDao.create(userId1, userId2);
            }
        } else {
            if (!friendship.isAccepted()) {
                friendshipDao.update(userId2, userId1, true);
            }
        }
    }

    public void deleteFriend(long userId1, long userId2) {
        Friendship friendship = friendshipDao.get(userId2, userId1);
        if (friendship == null) {
            friendship = friendshipDao.get(userId1, userId2);
            if (friendship == null) {
                throw new FriendshipNotFoundException();
            }
            friendshipDao.remove(userId1, userId2);
            friendshipDao.create(userId2, userId1);
        } else {
            friendshipDao.update(userId2, userId1, false);
        }
    }

    public List<User> getUserFriends(long id) {
        return friendshipDao.getByUserId(id).stream()
                .map(friendship -> getFriedFromFriendship(friendship, id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getUsersCommonFriends(long userId1, long userId2) {
        Set<User> friendsOfUser1 = new HashSet<>(getUserFriends(userId1));
        Set<User> friendsOfUser2 = new HashSet<>(getUserFriends(userId2));
        friendsOfUser1.retainAll(friendsOfUser2);
        return new ArrayList<>(friendsOfUser1);
    }

    private User getFriedFromFriendship(Friendship friendship, long userId) {
        if (friendship.getActiveUserId() == userId) {
            return userStorage.getUser(friendship.getPassiveUserId());
        } else if (friendship.isAccepted()) {
            return userStorage.getUser(friendship.getActiveUserId());
        } else {
            return null;
        }
    }
}