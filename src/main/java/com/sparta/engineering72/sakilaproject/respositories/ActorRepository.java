package com.sparta.engineering72.sakilaproject.respositories;

import com.sparta.engineering72.sakilaproject.entities.Actor;
import com.sparta.engineering72.sakilaproject.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer>{
    @Query(value = "SELECT * FROM actor WHERE first_name LIKE %:firstName% AND last_name LIKE %:lastName%", nativeQuery = true)
    List<Actor> findActorsByFirstNameAndLastNameContaining(String firstName, String lastName);
    
    @Query(value = "SELECT * FROM actor WHERE first_name LIKE %:firstName%", nativeQuery = true)
    List<Actor> findActorsByFirstNameContaining(String firstName);
    
    @Query(value = "SELECT * FROM actor WHERE last_name LIKE %:lastName%", nativeQuery = true)
    List<Actor> findActorsByLastNameContaining(String lastName);
    
    Actor getActorByActorId(Integer id);
}
