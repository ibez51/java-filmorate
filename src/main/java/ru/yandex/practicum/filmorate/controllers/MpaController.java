package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    public MpaController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping("/{id}")
    public MpaRating getRatingById(@PathVariable(name = "id") Integer mpaId) {
        return mpaRatingService.getRatingById(mpaId);
    }

    @GetMapping
    public List<MpaRating> getAllRatings() {
        return mpaRatingService.getAllRatings();
    }
}
