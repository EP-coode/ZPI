package com.core.backend.PostCategoryGroup;

import com.core.backend.utilis.NoIdException;
import com.core.backend.utilis.Utilis;
import com.core.backend.utilis.WrongIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/postCategoryGroup")
public class PostCategoryGroupController {

    @Autowired
    private PostCategoryGroupRepository postCategoryGroupRepository;
    @Autowired
    private Utilis utilis;

    @GetMapping()
    public ResponseEntity<Object> getAllPostCategoryGroup() {
        return new ResponseEntity<>(postCategoryGroupRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "{postCategoryId}")
    public ResponseEntity<Object> getPostCategoryGroup(@PathVariable String postCategoryId) {
        long longId;
        try {
            longId = utilis.convertId(postCategoryId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<PostCategoryGroup> postCategoryGroup = postCategoryGroupRepository.findById(longId);
        if (postCategoryGroup.isEmpty())
            return new ResponseEntity<>("Grupa o podanym id nie istnieje", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(postCategoryGroup.get(), HttpStatus.OK);
    }
}
