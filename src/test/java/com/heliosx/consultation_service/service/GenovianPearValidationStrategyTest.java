package com.heliosx.consultation_service.service;

import com.heliosx.consultation_service.model.domain.PrescriptionBlockers;
import com.heliosx.consultation_service.model.dto.request.QuestionAnswerDto;
import com.heliosx.consultation_service.service.validationStrategies.GenovianPearValidationStrategy;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.heliosx.consultation_service.constants.constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenovianPearValidationStrategyTest {

    GenovianPearValidationStrategy genovianPearValidationStrategy = new GenovianPearValidationStrategy();

    @Test
    public void givenQuestionSWithBlockingAnswers_returnsSetOfPrescriptionBlockers() {
        Map<Long, QuestionAnswerDto> questionAnswers = new HashMap<>();
        questionAnswers.put(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID, QuestionAnswerDto.builder()
                .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                .answer(true)
                .build());
        questionAnswers.put(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID, QuestionAnswerDto.builder()
                .questionId(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                .answer(true)
                .build());

        Set<PrescriptionBlockers> prescriptionBlockers = genovianPearValidationStrategy.validate(questionAnswers);

        assertEquals(2, prescriptionBlockers.size());
        assertEquals(1, prescriptionBlockers.stream()
                .filter(blockers -> Objects.equals(blockers.getQuestionId(), REACTION_TO_GENOVIAN_PEAR_QUESTION_ID))
                .count());
        assertEquals(1, prescriptionBlockers.stream()
                .filter(blockers -> Objects.equals(blockers.getQuestionId(), MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID))
                .count());
    }

    @Test
    public void givenQuestionsWithSingleBlockingAnswer_returnsSetOfPrescriptionBlockers() {
        Map<Long, QuestionAnswerDto> questionAnswers = new HashMap<>();
        questionAnswers.put(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID, QuestionAnswerDto.builder()
                .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                .answer(true)
                .build());
        questionAnswers.put(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID, QuestionAnswerDto.builder()
                .questionId(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                .answer(false)
                .build());

        Set<PrescriptionBlockers> prescriptionBlockers = genovianPearValidationStrategy.validate(questionAnswers);

        assertEquals(1, prescriptionBlockers.size());
        assertEquals(1, prescriptionBlockers.stream()
                .filter(blockers -> Objects.equals(blockers.getQuestionId(), REACTION_TO_GENOVIAN_PEAR_QUESTION_ID))
                .count());
    }

    @Test
    public void givenQuestionsWithNoBlockingAnswers_returnsEmptySetOfPrescriptionBlockers() {
        Map<Long, QuestionAnswerDto> questionAnswers = new HashMap<>();
        questionAnswers.put(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID, QuestionAnswerDto.builder()
                .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                .answer(false)
                .build());
        questionAnswers.put(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID, QuestionAnswerDto.builder()
                .questionId(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                .answer(false)
                .build());

        Set<PrescriptionBlockers> prescriptionBlockers = genovianPearValidationStrategy.validate(questionAnswers);

        assertEquals(0, prescriptionBlockers.size());
    }
}
