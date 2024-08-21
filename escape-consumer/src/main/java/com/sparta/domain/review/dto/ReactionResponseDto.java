package com.sparta.domain.review.dto;

import com.sparta.domain.reaction.entity.ReactionType;
import lombok.Getter;

@Getter
public class ReactionResponseDto {
    private ReactionType reactionType;
    private Boolean reactionStatus;

    public ReactionResponseDto(ReactionType reactionType, Boolean reactionStatus){
        this.reactionType = reactionType;
        this.reactionStatus = reactionStatus;
    }
}
