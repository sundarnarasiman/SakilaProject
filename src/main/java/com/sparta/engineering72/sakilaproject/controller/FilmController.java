package com.sparta.engineering72.sakilaproject.controller;

import com.sparta.engineering72.sakilaproject.entities.Customer;
import com.sparta.engineering72.sakilaproject.entities.Film;
import com.sparta.engineering72.sakilaproject.entities.Inventory;
import com.sparta.engineering72.sakilaproject.entities.Rental;
import com.sparta.engineering72.sakilaproject.services.CustomerService;
import com.sparta.engineering72.sakilaproject.services.FilmService;
import com.sparta.engineering72.sakilaproject.services.InventoryService;
import com.sparta.engineering72.sakilaproject.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Controller
public class FilmController {

    private FilmService filmService;
    private InventoryService inventoryService;
    private RentalService rentalService;
    private CustomerService customerService;
    private com.sparta.engineering72.sakilaproject.services.LanguageService languageService;

    @Autowired
    public FilmController(FilmService filmService, InventoryService inventoryService, RentalService rentalService, CustomerService customerService, com.sparta.engineering72.sakilaproject.services.LanguageService languageService) {
        this.filmService = filmService;
        this.inventoryService = inventoryService;
        this.rentalService = rentalService;
        this.customerService = customerService;
        this.languageService = languageService;
    }

    @GetMapping("/films")
    public String getFilms(ModelMap modelMap, 
                          @RequestParam(value = "title", defaultValue = "") String titleFilter,
                          @RequestParam(value = "language", defaultValue = "") String languageFilter) {
        List<Film> films;
        if (titleFilter.isEmpty() && languageFilter.isEmpty()){
            films = filmService.getAllFilms();
        } else if (languageFilter.isEmpty()){
            films = filmService.getFilmsByTitle(titleFilter);
        } else if (titleFilter.isEmpty()){
            films = filmService.getFilmsByLanguageName(languageFilter);
        } else {
            films = filmService.getFilmsByTitleAndLanguage(titleFilter, languageFilter);
        }
        modelMap.addAttribute("films", films);
        modelMap.addAttribute("availableFilms", filmService.getAvailableFilms());
        modelMap.addAttribute("searchTerm", titleFilter);
        modelMap.addAttribute("languageSearch", languageFilter);
        return "/films/films";
    }

    @GetMapping("/films/details")
    public String getFilmDetails(ModelMap modelMap, @RequestParam(value = "id") Integer id) {
        Film film = filmService.getFilmByID(id);
        boolean available = filmService.getAvailableFilms().contains(film);
        modelMap.addAttribute("available", available);
        modelMap.addAttribute("details", film);
        return "films/filmDetails";
    }

    @GetMapping("/rent/{filmid}")
    public String rentFilm(ModelMap modelMap, Principal principal,
                           @PathVariable("filmid") int filmid){
        String name = principal.getName();
        Customer customer = customerService.getCustomerByEmail(name);
        List<Inventory> availableInventory = inventoryService.getAvailableInventoryByFilmId(filmid);
        if (!availableInventory.isEmpty()){
            Inventory inventory = availableInventory.get(0);
            LocalDateTime returnDate = LocalDateTime.now().plusDays(filmService.getFilmByID(inventory.getFilmId()).getRentalDuration());
            rentalService.addRental(inventory.getInventoryId(), customer.getCustomerId(), Timestamp.valueOf(returnDate));
        }
        return "redirect:/films";
    }

    @GetMapping("/owner/manage-films")
    public String getFilmDetails(ModelMap modelMap,
                                 @RequestParam(value = "title", defaultValue = "") String filter) {


        List<Film> films;
        if (filter.isEmpty()){
            films = filmService.getAllFilms();
        }
        else {
            films = filmService.getFilmsByTitle(filter);
        }

        HashMap<Integer, Integer> filmCount = new HashMap<>();
        for(Film film : films){
            filmCount.put(film.getFilmId(), filmService.getAvailableFilmCount(film.getFilmId()));
        }

        modelMap.addAttribute("films", films);
        modelMap.addAttribute("filmCount", filmCount);
        modelMap.addAttribute("searchTerm", filter);
        return "/owner/manage-films";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("/owner/edit-film");
        Film film = filmService.getFilmByID(id);
        mav.addObject("film", film);

        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        filmService.deleteFilmById(id);
        return "redirect:/owner/manage-films";
    }

    @GetMapping("/add-film")
    public String showAddFilmPage(ModelMap modelMap) {
        Film film = new Film();
        modelMap.addAttribute("film", film);
        return "/owner/add-film";
    }

    @RequestMapping("/save")
    public String saveFilm(Film film) {
        filmService.save(film);
        return "redirect:/owner/manage-films";
    }


    public Film findFilmByID(Integer id) {
        return filmService.getFilmByID(id);
    }

}
