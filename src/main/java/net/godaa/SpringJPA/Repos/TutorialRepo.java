package net.godaa.SpringJPA.Repos;

import net.godaa.SpringJPA.models.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TutorialRepo extends JpaRepository<Tutorial, Long> {

        List<Tutorial> findByPublished(boolean published);
        List<Tutorial> findByTitle(String title);

}
