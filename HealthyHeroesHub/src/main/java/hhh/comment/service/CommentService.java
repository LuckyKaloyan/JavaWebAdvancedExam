package hhh.comment.service;
import hhh.comment.model.Comment;
import hhh.comment.repository.CommentRepository;
import hhh.meal.service.MealService;
import hhh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final MealService mealService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, MealService mealService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.mealService = mealService;
    }

    public void createComment(String text, UUID userId, UUID mealId) {
        Comment comment = Comment.builder()
                .user(userService.getById(userId))
                .meal(mealService.getMealById(mealId))
                .text(text)
                .createdOn(LocalDate.now())
                .build();
        commentRepository.save(comment);
    }



}
