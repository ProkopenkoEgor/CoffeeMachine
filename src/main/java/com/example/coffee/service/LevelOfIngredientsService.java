package com.example.coffee.service;

import com.example.coffee.model.LevelOfIngredients;
import com.example.coffee.repository.LevelofingredientsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelOfIngredientsService {
    private final LevelofingredientsRepository levelofingredientsRepository;

    public LevelOfIngredientsService(LevelofingredientsRepository levelofingredientsRepository) {
        this.levelofingredientsRepository = levelofingredientsRepository;
    }

    public LevelOfIngredients findById(Long id) {
        return levelofingredientsRepository.findById(id).orElse(null);
    }

    public List<LevelOfIngredients> findAll(){
        return levelofingredientsRepository.findAll();
    }

    public LevelOfIngredients saveLevelofingredients(LevelOfIngredients levelofingredients){
        return levelofingredientsRepository.save(levelofingredients);
    }

    public void deleteById(Long id){
        levelofingredientsRepository.deleteById(id);
    }
}
