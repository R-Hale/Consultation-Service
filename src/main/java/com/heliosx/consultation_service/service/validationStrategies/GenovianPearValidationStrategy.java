package com.heliosx.consultation_service.service.validationStrategies;

import com.heliosx.consultation_service.config.ConditionStrategy;
import com.heliosx.consultation_service.model.domain.PrescriptionBlockers;
import com.heliosx.consultation_service.model.dto.request.QuestionAnswerDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.heliosx.consultation_service.constants.constants.*;
import static com.heliosx.consultation_service.service.ConsultationQuestionsService.addQuestionValidationStrategy;

@Service
public class GenovianPearValidationStrategy implements QuestionValidationStrategy {

    @PostConstruct
    @Override
    public void register() {
        addQuestionValidationStrategy(ConditionStrategy.GENOVIAN_PEAR,this);
    }

    @Override
    public Set<PrescriptionBlockers> validate(Map<Long, QuestionAnswerDto> questionAnswers) {
        Set<PrescriptionBlockers> prescriptionBlockers = new HashSet<>();

        if (questionAnswers.get(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID).isAnswer()) {
            prescriptionBlockers.add(
                    new PrescriptionBlockers(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID, PRESCRIPTION_BLOCKER_MSG_NO_SYMPTOMS)
            );
        }

        if (questionAnswers.get(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID).isAnswer()) {
            prescriptionBlockers.add(
                    new PrescriptionBlockers(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID, PRESCRIPTION_BLOCKER_MSG_MEDICATION_PREVIOUSLY_PRESCRIBED)
            );
        }

        return prescriptionBlockers;
    }
}
