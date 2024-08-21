package com.sparta.domain.review.dto;

import com.sparta.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String title;
    private String contents;
    private Double rating;
    private String email;
    private String storeName;
    private String themeName;
    private String themeImage;

    public ReviewResponseDto(Review review){
        this.reviewId = review.getId();
        this.title = review.getTitle();
        this.contents = review.getContents();
        this.rating = review.getRating();
        this.email = review.getUser().getEmail();
        this.storeName = review.getTheme().getStore().getName();
        this.themeName = review.getTheme().getTitle();
        this.themeImage = review.getTheme().getThemeImage();
    }
}
