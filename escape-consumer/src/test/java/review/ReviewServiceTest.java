package review;

import com.sparta.domain.reaction.entity.Reaction;
import com.sparta.domain.reaction.entity.ReactionType;
import com.sparta.domain.reaction.repository.ReactionRepository;
import com.sparta.domain.review.dto.ReactionResponseDto;
import com.sparta.domain.review.entity.Review;
import com.sparta.domain.review.repository.ReviewRepository;
import com.sparta.domain.review.service.ReviewService;
import com.sparta.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Review review;
    private Reaction reaction;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        review = mock(Review.class);
        reaction = Reaction.builder().reactionType(ReactionType.GOOD).user(user).review(review).build();
    }

    @Test
    public void 리액션_등록() {
        //given
        when(reviewRepository.findByIdOrElse(any())).thenReturn(review);
        when(reactionRepository.findByReviewAndUser(any(), any())).thenReturn(Optional.empty());
        when(reactionRepository.save(any())).thenReturn(reaction);

        //then
        ReactionResponseDto response = reviewService.createReaction(1L, ReactionType.GOOD, user);

        //then
        verify(reviewRepository, times(1)).findByIdOrElse(1L);
        verify(reactionRepository, times(1)).findByReviewAndUser(review, user);
        verify(reactionRepository, times(1)).save(any(Reaction.class));
        assertThat(response.getReactionType()).isEqualTo(reaction.getReactionType());
    }

    @Test
    public void 리액션_수정() {
        //given
        when(reviewRepository.findByIdOrElse(any())).thenReturn(review);
        when(reactionRepository.findByReviewAndUser(any(), any())).thenReturn(Optional.of(reaction));

        //when
        ReactionResponseDto response = reviewService.createReaction(1L, ReactionType.BAD, user);

        //then
        verify(reviewRepository, times(1)).findByIdOrElse(1L);
        verify(reactionRepository, times(1)).findByReviewAndUser(review, user);
        assertThat(response.getReactionType()).isEqualTo(reaction.getReactionType());
    }

    @Test
    public void 리액션_삭제() {
        //given
        when(reviewRepository.findByIdOrElse(any())).thenReturn(review);
        when(reactionRepository.findByReviewAndUser(any(), any())).thenReturn(Optional.of(reaction));

        //when
        ReactionResponseDto response = reviewService.createReaction(1L, ReactionType.GOOD, user);

        //then
        verify(reviewRepository, times(1)).findByIdOrElse(1L);
        verify(reactionRepository, times(1)).findByReviewAndUser(review, user);
        verify(reactionRepository, times(1)).delete(reaction);
        assertThat(response.getReactionStatus()).isEqualTo(false);
    }
}
