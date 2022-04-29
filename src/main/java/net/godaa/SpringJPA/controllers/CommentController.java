package net.godaa.SpringJPA.controllers;

import net.godaa.SpringJPA.Repos.CommentRepo;
import net.godaa.SpringJPA.Repos.TutorialRepo;
import net.godaa.SpringJPA.models.Comment;
import net.godaa.SpringJPA.models.Tutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    TutorialRepo tutorialRepo;
    @Autowired
    CommentRepo commentRepo;

    @GetMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {
        if (!tutorialRepo.existsById(tutorialId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
        } else {
            List<Comment> comments = commentRepo.findByTutorialId(tutorialId);
            return new ResponseEntity<>(comments, HttpStatus.OK);

        }
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentByTutorialId(@PathVariable(value = "id") Long id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Comment with id = " + id));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<HttpStatus> createComment(
            @RequestBody Comment commentBody,
            @PathVariable(value = "tutorialId") Long tutorialId) {
        tutorialRepo.findById(tutorialId).map(
                tutorial -> {
                    commentBody.setTutorial(tutorial);
                    return commentRepo.save(commentBody);
                }).orElseThrow(() ->
                new ResourceNotFoundException("NOT FOUND TUTORIAL WITH ID" + tutorialId));
        return new ResponseEntity<>(HttpStatus.OK);

    }

    ;

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") long id, @RequestBody Comment commentRequest) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));
        comment.setContent(commentRequest.getContent());
        return new ResponseEntity<>(commentRepo.save(comment), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id) {
        commentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> deleteAllCommentsOfTutorial(@PathVariable(value = "tutorialId") Long tutorialId) {
        if (!tutorialRepo.existsById(tutorialId)) {
            throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
        }
        commentRepo.deleteByTutorialId(tutorialId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
