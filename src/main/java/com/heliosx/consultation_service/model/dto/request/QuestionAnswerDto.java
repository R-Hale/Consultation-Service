package com.heliosx.consultation_service.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionAnswerDto {
    private long questionId;
    private boolean answer;
}
