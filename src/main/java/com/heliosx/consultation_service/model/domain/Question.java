package com.heliosx.consultation_service.model.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Question {
    private Long id;
    private String questionText;
}
