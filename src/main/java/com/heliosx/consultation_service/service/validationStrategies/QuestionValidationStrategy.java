package com.heliosx.consultation_service.service.validationStrategies;

import com.heliosx.consultation_service.model.domain.PrescriptionBlockers;
import com.heliosx.consultation_service.model.dto.request.QuestionAnswerDto;

import java.util.Map;
import java.util.Set;

public interface QuestionValidationStrategy {
    void register();
    Set<PrescriptionBlockers> validate(Map<Long, QuestionAnswerDto> questionAnswers);
}
