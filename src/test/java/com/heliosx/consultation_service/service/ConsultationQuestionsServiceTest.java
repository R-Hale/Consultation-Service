package com.heliosx.consultation_service.service;

import com.heliosx.consultation_service.exceptions.InvalidConditionException;
import com.heliosx.consultation_service.model.domain.PrescriptionBlockers;
import com.heliosx.consultation_service.model.dto.request.ConsultationFormDto;
import com.heliosx.consultation_service.model.dto.request.QuestionAnswerDto;
import com.heliosx.consultation_service.model.domain.Question;
import com.heliosx.consultation_service.service.validationStrategies.GenovianPearValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.heliosx.consultation_service.config.ConditionStrategy.GENOVIAN_PEAR;
import static com.heliosx.consultation_service.constants.constants.*;
import static com.heliosx.consultation_service.integration.ConsultationControllerTests.GENOVIAN_PEAR_CONDITION_ID;
import static com.heliosx.consultation_service.test_config.constants.ENUM_GENOVIAN_PEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ConsultationQuestionsServiceTest {

    @InjectMocks
    ConsultationQuestionsService consultationQuestionsService;

    @Mock
    ConsultationQuestionsCache consultationQuestionsCache;

    @Mock
    GenovianPearValidationStrategy genovianPearValidationStrategy;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        consultationQuestionsService = new ConsultationQuestionsService(consultationQuestionsCache);
        ConsultationQuestionsService.addQuestionValidationStrategy(GENOVIAN_PEAR, genovianPearValidationStrategy);
    }

    @Test
    public void givenValidConditionId_getConsultationQuestionsReturnsListOfQuestions() {
        when(consultationQuestionsCache.getQuestions(eq(GENOVIAN_PEAR_CONDITION_ID))).thenReturn(Optional.of(List.of(
                Question.builder()
                        .id(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .questionText(REACTION_TO_GENOVIAN_PEAR_QUESTION_TEXT)
                        .build()
        )));

        List<Question> questions = consultationQuestionsService.getConsultationQuestions(GENOVIAN_PEAR_CONDITION_ID);

        assertEquals(1, questions.size());
    }

    @Test
    public void givenInvalidConditionId_getConsultationQuestionsThrowsException() {
        Long invalidConditionId = 0L;

        when(consultationQuestionsCache.getQuestions(eq(invalidConditionId))).thenReturn(Optional.ofNullable(null));

        assertThrows(InvalidConditionException.class, () -> consultationQuestionsService.getConsultationQuestions(invalidConditionId));
    }

    @Test
    public void givenSubmittedQuestionsWithBlockingAnswers_returnsSetOfPrescriptionBlockers() {
        when(consultationQuestionsCache.getQuestions(eq(GENOVIAN_PEAR_CONDITION_ID))).thenReturn(Optional.of(List.of(
                Question.builder()
                        .id(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .questionText(REACTION_TO_GENOVIAN_PEAR_QUESTION_TEXT)
                        .build(),
                Question.builder()
                        .id(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                        .questionText(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_TEXT)
                        .build()
        )));

        when(consultationQuestionsCache.getConditionNames(eq(GENOVIAN_PEAR_CONDITION_ID))).thenReturn(Optional.of(ENUM_GENOVIAN_PEAR));

        when(genovianPearValidationStrategy.validate(any())).thenReturn(Set.of(
                PrescriptionBlockers.builder()
                        .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .reason(PRESCRIPTION_BLOCKER_MSG_NO_SYMPTOMS)
                        .build(),
                PrescriptionBlockers.builder()
                        .questionId(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                        .reason(PRESCRIPTION_BLOCKER_MSG_MEDICATION_PREVIOUSLY_PRESCRIBED)
                        .build()
        ));

        List<QuestionAnswerDto> questionAnswersDto = List.of(
                QuestionAnswerDto.builder()
                        .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .answer(true)
                        .build(),
                QuestionAnswerDto.builder()
                        .questionId(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                        .answer(true)
                        .build()
        );

        ConsultationFormDto consultationFormDto = ConsultationFormDto.builder()
                .medicalConditionId(GENOVIAN_PEAR_CONDITION_ID)
                .questionAnswers(questionAnswersDto)
                .build();

        Set<PrescriptionBlockers> prescriptionBlockers = consultationQuestionsService.assessConsultationQuestions(consultationFormDto);

        assertEquals(2, prescriptionBlockers.size());
    }
}
