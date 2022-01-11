package com.example.tutorial.controller;

import com.example.tutorial.model.Tutorial;
import com.example.tutorial.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "localhost://8081")
@RestController
@RequestMapping("/api")
public class TutorialController {

    final TutorialRepository tutorialRepository;

    public TutorialController(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false)String title) {
    try {
        List<Tutorial> tutorials = new ArrayList<Tutorial>();

        if (title == null) {
            tutorialRepository.findAll().forEach(tutorials::add);
        } else {
            tutorials.addAll(tutorialRepository.findByTitleContaining(title));
        }
        if (tutorials.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorials){
        try {
            //Tutorial tutorials = new Tutorial();
            tutorialRepository.save(new Tutorial(tutorials.getTitle(), tutorials.getDescription(), false));
            return new ResponseEntity<>(tutorials, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") Long id, @RequestBody Tutorial tutorial) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            Tutorial tutorials = tutorialData.get();
            tutorials.setTitle(tutorial.getTitle());
            tutorials.setDescription(tutorial.getDescription());
            tutorials.setPublished(tutorial.isPublished());
            return new ResponseEntity<>(tutorialRepository.save(tutorials), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        @DeleteMapping("/tutorials/{id}")
        public ResponseEntity<Tutorial> deleteTutorial(@PathVariable("id") Long id, @RequestBody Tutorial tutorial){
            try{
                tutorialRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @DeleteMapping("/tutorials")
        public ResponseEntity<Tutorial> deleteAllTutorials(){
            try{
                tutorialRepository.deleteAll();
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping("/tutorials/published")
        public ResponseEntity<Tutorial> findByPublished(){
            try{
                List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

                if(tutorials.isEmpty())
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
