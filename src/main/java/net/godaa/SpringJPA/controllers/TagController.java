package net.godaa.SpringJPA.controllers;

import net.godaa.SpringJPA.Repos.TagRepo;
import net.godaa.SpringJPA.Repos.TutorialRepo;
import net.godaa.SpringJPA.models.Tag;
import net.godaa.SpringJPA.models.Tutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TagController {
    @Autowired
    TutorialRepo tutorialRepo;
    @Autowired
    TagRepo tagRepo;

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = new ArrayList<Tag>(tagRepo.findAll());
        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(tags, HttpStatus.OK);
        }

    }

    @GetMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<List<Tag>> getAllTagsByTutorialID(@PathVariable("tutorialId") Long tutorialId) {
        if (!tutorialRepo.existsById(tutorialId)) {
            throw new ResourceNotFoundException("Not found Tutorials with id = " + tutorialId);
        }
        List<Tag> tags = tagRepo.findTagsByTutorialsId(tutorialId);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<Tag> getTagByID(@PathVariable("tagId") Long tagId) {
        Tag tag = tagRepo.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id : " + tagId));
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PostMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<Tag> addTagToTutorial(@PathVariable(value = "tutorialId") Long tutorialId, @RequestBody Tag tagRequest) {
        if (tutorialRepo.existsById(tutorialId)) {
            if (tagRepo.existsById(tagRequest.getId())) {
                Tutorial tutorial = tutorialRepo.findById(tutorialId).orElseThrow();
                tutorial.addTag(tagRequest);

            } else {
                tagRepo.save(tagRequest);
                Tutorial tutorial = tutorialRepo.findById(tutorialId).orElseThrow();
                tutorial.addTag(tagRequest);
            }

        } else {
            throw new ResourceNotFoundException("Not found Tutorials with id = " + tutorialId);
        }


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") long id, @RequestBody Tag tagRequest) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TagId " + id + "not found"));
        tag.setName(tagRequest.getName());
        return new ResponseEntity<>(tagRepo.save(tag), HttpStatus.OK);
    }


    @DeleteMapping("/tutorials/{tutorialId}/tags/{tagId}")
    public ResponseEntity<HttpStatus> deleteTagFromTutorial(@PathVariable(value = "tutorialId") Long tutorialId, @PathVariable(value = "tagId") Long tagId) {
        Tutorial tutorial = tutorialRepo.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

        tutorial.removeTag(tagId);
        tutorialRepo.save(tutorial);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {
        tagRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

