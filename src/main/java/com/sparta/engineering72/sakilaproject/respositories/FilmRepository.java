package com.sparta.engineering72.sakilaproject.respositories;

import com.sparta.engineering72.sakilaproject.entities.Actor;
import com.sparta.engineering72.sakilaproject.entities.Customer;
import com.sparta.engineering72.sakilaproject.entities.Film;
import com.sparta.engineering72.sakilaproject.entities.FilmActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query(value = "SELECT count(*) FROM inventory i WHERE i.film_id = :id AND i.inventory_id NOT IN (SELECT r.inventory_id FROM rental r WHERE r.return_date IS NULL OR r.return_date > NOW())",
            nativeQuery = true)
    Integer getAvailableFilmCount(Integer id);

    @Query(value = "SELECT DISTINCT f.* FROM film f INNER JOIN inventory i ON f.film_id = i.film_id WHERE i.inventory_id NOT IN (SELECT r.inventory_id FROM rental r WHERE r.return_date IS NULL OR r.return_date > NOW())",
            nativeQuery = true)
    List<Film> getAvailableFilms();

    @Query(value = "SELECT * FROM film f INNER JOIN film_category fc ON f.film_id = fc.film_id INNER JOIN category c ON fc.category_id = c.category_id WHERE c.category_id = :categoryId",
            nativeQuery = true)
    List<Film> getAllFilmsByCategory(Integer categoryId);

    @Query(value = "SELECT * FROM actor a INNER JOIN film_actor fa ON a.actor_id = fa.actor_id INNER JOIN film f ON fa.film_id = f.film_id WHERE a.actor_id = :actorId",
            nativeQuery = true)
    List<Film> getAllFilmsByActor(Integer actorId);

    @Query(value = "SELECT * FROM film WHERE title LIKE %:title%", nativeQuery = true)
    List<Film> findByTitleContaining(String title);
    
    @Query(value = "SELECT f.* FROM film f JOIN language l ON f.language_id = l.language_id WHERE l.name LIKE %:languageName%", nativeQuery = true)
    List<Film> findByLanguageName(String languageName);
    
    @Query(value = "SELECT f.* FROM film f JOIN language l ON f.language_id = l.language_id WHERE f.title LIKE %:title% AND l.name LIKE %:languageName%", nativeQuery = true)
    List<Film> findByTitleAndLanguageName(String title, String languageName);
    
    Film getFilmByFilmId(Integer id);

}
