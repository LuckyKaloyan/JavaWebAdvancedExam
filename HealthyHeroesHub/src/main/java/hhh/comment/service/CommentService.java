package hhh.comment.service;
import hhh.comment.model.Comment;
import hhh.comment.repository.CommentRepository;
import hhh.meal.service.MealService;
import hhh.user.model.User;
import hhh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void deleteCommentById(UUID id) {
        commentRepository.deleteById(id);
    }
    public List<Comment> getAllCreatedOnLastYear() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        return commentRepository.findByCreatedOnAfter(oneYearAgo);
    }
    public List<Comment> getAllCreatedOnLastMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        return commentRepository.findByCreatedOnAfter(oneMonthAgo);
    }
    public List<Comment> getAllCreatedOnLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        return commentRepository.findByCreatedOnAfter(oneWeekAgo);
    }
    public List<Comment> getAllCreatedOnLast24hours() {
        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
        return commentRepository.findByCreatedOnAfter(oneDayAgo);
    }
}