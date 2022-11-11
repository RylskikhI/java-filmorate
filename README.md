ER-диаграмма для проекта 

![Scheme](https://github.com/RylskikhI/java-filmorate/raw/add-database/Scheme.png)

Примеры запросов:

1. Получение всех фильмов:

   ```SELECT * FROM film```
        
2. Получение всех пользователей:

   ```SELECT * FROM user```
       
3. Получение любимых жанров пользователя с id1:

   ```
   SELECT g.name, COUNT(*)
   FROM films AS f
   JOIN likes AS l ON f.id = l.film_id
   JOIN film_genres AS fg ON f.id = fg.film_id
   JOIN genres AS g ON fg.genre_id = g.id
   WHERE l.user_id = [id1]
   GROUP BY g.name
   ORDER BY COUNT(*) DESC;
   ```

4. Получение топ-10 популярных фильмов:
   
   ```
   SELECT f.id, f.NAME, COUNT(*)
   FROM films AS f
   LEFT JOIN likes AS l ON f.id = l.film_id
   GROUP BY f.name
   ORDER BY
   CASE WHEN l.film_id IS NULL THEN 1 ELSE 0 END,
   COUNT(*) DESC
   LIMIT 10;
   ```

      
       
    