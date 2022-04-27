package net.godaa.SpringJPA.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorialRepo extends JpaRepository<Tutorial, Long> {

        List<Tutorial> findByPublished(boolean published);
        List<Tutorial> findByTitle(String title);

}
