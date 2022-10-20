package com.core.backend.PostCategory;

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
@RequestMapping(path = "postCategory")
public class PostCategoryController {

    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private Utilis utilis;

    @GetMapping
    public ResponseEntity<Object> getAllPostCategoryController() {
        return new ResponseEntity<>(postCategoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "{postCategoryId}")
    public ResponseEntity<Object> getPostCategory(@PathVariable String postCategoryId) {
        long longId;
        try {
            longId = utilis.convertId(postCategoryId);
        } catch (WrongIdException e) {
            return new ResponseEntity<>("Brak wartości dla pola id", HttpStatus.BAD_REQUEST);
        } catch (NoIdException e) {
            return new ResponseEntity<>("Podane id nie jest liczbą", HttpStatus.BAD_REQUEST);
        }
        Optional<PostCategory> postCategory = postCategoryRepository.findById(longId);
        if (postCategory.isEmpty())
            return new ResponseEntity<>("Brak kategorii postu o podanym id", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(postCategory.get(), HttpStatus.OK);
    }
}
