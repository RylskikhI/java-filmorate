ER-диаграмма для проекта 

![Scheme](https://github.com/RylskikhI/java-filmorate/raw/add-database/Scheme.png)

Примеры запросов:

1. Получение всех фильмов:

   `SELECT * FROM film`
        
2. Получение всех пользователей:

   `SELECT * FROM user`
       
3. Получение 10 самых популярных фильмов:

   ```
   SELECT
   f.film_id as film_id,
   f.title as title,
   COUNT(uf.user_id) as top
   FROM film AS f
   INNER JOIN user_films uf ON uf.film_id = f.film_id
   GROUP BY
   film_id,
   title
   ORDER BY top DESC
   LIMIT 10;
   ```

4. Список общих друзей:
   
   ```
   SELECT
   uf.friend_id
   FROM user_friends AS uf
   WHERE uf.user_id = 1 -- ID первого пользователя
   AND uf.friendship_id = 1 -- 1 означает подтвержденный статус
   AND uf.friend_id IN (
   SELECT friend_id
   FROM user_friends uf1
   WHERE uf1.user_id = 2 -- ID второго пользователя
   uf1.friendship_id = 1 -- 1 означает подтвержденный статус
   )
   ```

      
       
    