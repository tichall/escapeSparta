package com.sparta.domain.review.dto;

import com.sparta.domain.reaction.entity.ReactionType;
import lombok.Getter;

@Getter
public class ReactionRequest {
    private ReactionType reactionType;
}
