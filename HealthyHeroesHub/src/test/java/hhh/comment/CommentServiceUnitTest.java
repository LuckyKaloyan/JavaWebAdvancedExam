package hhh.comment;

import hhh.comment.model.Comment;
import hhh.comment.repository.CommentRepository;
import hhh.comment.service.CommentService;
import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.meal.service.MealService;
import hhh.user.model.User;
import hhh.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private MealService mealService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_shouldThrowExceptionWhenTextIsEmpty() {
        assertThrows(BadInputException.class, () ->
                commentService.createComment("", UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void createComment_shouldThrowExceptionWhenUserIdIsNull() {
        assertThrows(BadInputException.class, () ->
                commentService.createComment("text", null, UUID.randomUUID()));
    }

    @Test
    void createComment_shouldThrowExceptionWhenMealIdIsNull() {
        assertThrows(BadInputException.class, () ->
                commentService.createComment("text", UUID.randomUUID(), null));
    }

    @Test
    void createComment_shouldSaveComment() {
        UUID userId = UUID.randomUUID();
        UUID mealId = UUID.randomUUID();
        User user = new User();
        Meal meal = new Meal();

        when(userService.getById(userId)).thenReturn(user);
        when(mealService.getMealById(mealId)).thenReturn(meal);

        commentService.createComment("test comment", userId, mealId);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getAllComments_shouldReturnAllComments() {
        List<Comment> comments = List.of(new Comment());
        when(commentRepository.findAll()).thenReturn(comments);
        assertEquals(comments, commentService.getAllComments());
    }

    @Test
    void deleteCommentById_shouldThrowExceptionWhenIdIsNull() {
        assertThrows(BadInputException.class, () -> commentService.deleteCommentById(null));
    }

    @Test
    void deleteCommentById_shouldDeleteComment() {
        UUID id = UUID.randomUUID();
        commentService.deleteCommentById(id);
        verify(commentRepository).deleteById(id);
    }

    @Test
    void getAllCreatedOnLastYear_shouldReturnRecentComments() {
        LocalDate date = LocalDate.now().minusYears(1);
        List<Comment> comments = List.of(new Comment());
        when(commentRepository.findByCreatedOnAfter(date)).thenReturn(comments);
        assertEquals(comments, commentService.getAllCreatedOnLastYear());
    }

    @Test
    void getAllCreatedOnLastMonth_shouldReturnRecentComments() {
        LocalDate date = LocalDate.now().minusMonths(1);
        List<Comment> comments = List.of(new Comment());
        when(commentRepository.findByCreatedOnAfter(date)).thenReturn(comments);
        assertEquals(comments, commentService.getAllCreatedOnLastMonth());
    }

    @Test
    void getAllCreatedOnLastWeek_shouldReturnRecentComments() {
        LocalDate date = LocalDate.now().minusDays(7);
        List<Comment> comments = List.of(new Comment());
        when(commentRepository.findByCreatedOnAfter(date)).thenReturn(comments);
        assertEquals(comments, commentService.getAllCreatedOnLastWeek());
    }

    @Test
    void getAllCreatedOnLast24hours_shouldReturnRecentComments() {
        LocalDate date = LocalDate.now().minusDays(1);
        List<Comment> comments = List.of(new Comment());
        when(commentRepository.findByCreatedOnAfter(date)).thenReturn(comments);
        assertEquals(comments, commentService.getAllCreatedOnLast24hours());
    }
}