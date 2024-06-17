package com.heliosx.consultation_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heliosx.consultation_service.model.dto.request.ConsultationFormDto;
import com.heliosx.consultation_service.model.dto.request.QuestionAnswerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;


import static com.heliosx.consultation_service.constants.constants.*;
import static com.heliosx.consultation_service.test_config.constants.GET_CONSULTATION_QUESTIONS_ENDPOINT;
import static com.heliosx.consultation_service.test_config.constants.POST_CONSULTATION_QUESTIONS_ENDPOINT;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConsultationControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public final static long GENOVIAN_PEAR_CONDITION_ID = 1;
    private final static long INVALID_CONDITION_ID = 0;

    @Test
    public void getQuestionsWithValidConditionId_ReturnsConsultationQuestions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_CONSULTATION_QUESTIONS_ENDPOINT, GENOVIAN_PEAR_CONDITION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].questionText").value(REACTION_TO_GENOVIAN_PEAR_QUESTION_TEXT))
                .andExpect(jsonPath("$[1].questionText").value(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_TEXT));
    }

    @Test
    public void getQuestionsWithInvalidConditionId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_CONSULTATION_QUESTIONS_ENDPOINT, INVALID_CONDITION_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(UNKNOWN_CONDITION_MSG));
    }

    @Test
    public void submitConsultationQuestionsWithBlockerAnswers_ReturnsPrescriptionBlockerReasons() throws Exception {
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

        String consultationQuestionAnswersJson = objectMapper.writeValueAsString(consultationFormDto);

        mockMvc.perform(MockMvcRequestBuilders.post(POST_CONSULTATION_QUESTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consultationQuestionAnswersJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].reason", hasItem(containsString(PRESCRIPTION_BLOCKER_MSG_NO_SYMPTOMS))))
                .andExpect(jsonPath("$[*].reason", hasItem(containsString(PRESCRIPTION_BLOCKER_MSG_MEDICATION_PREVIOUSLY_PRESCRIBED))));
    }

    @Test
    public void submitConsultationQuestionsWithNoBlockingAnswers_ReturnsNoPrescriptionBlockerReasons() throws Exception {
        List<QuestionAnswerDto> questionAnswersDto = List.of(
                QuestionAnswerDto.builder()
                        .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .answer(false)
                        .build(),
                QuestionAnswerDto.builder()
                        .questionId(MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID)
                        .answer(false)
                        .build()
        );

        ConsultationFormDto consultationFormDto = ConsultationFormDto.builder()
                .medicalConditionId(GENOVIAN_PEAR_CONDITION_ID)
                .questionAnswers(questionAnswersDto)
                .build();

        String consultationQuestionAnswersJson = objectMapper.writeValueAsString(consultationFormDto);

        mockMvc.perform(MockMvcRequestBuilders.post(POST_CONSULTATION_QUESTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consultationQuestionAnswersJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].reason").doesNotExist())
                .andExpect(jsonPath("$[*].reason").doesNotHaveJsonPath());
    }

    @Test
    public void submitConsultationQuestionsWithDuplicateAnswers_ReturnsBadRequest() throws Exception {
        List<QuestionAnswerDto> questionAnswersDto = List.of(
                QuestionAnswerDto.builder()
                        .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .answer(false)
                        .build(),
                QuestionAnswerDto.builder()
                        .questionId(REACTION_TO_GENOVIAN_PEAR_QUESTION_ID)
                        .answer(false)
                        .build()
        );

        ConsultationFormDto consultationFormDto = ConsultationFormDto.builder()
                .medicalConditionId(GENOVIAN_PEAR_CONDITION_ID)
                .questionAnswers(questionAnswersDto)
                .build();

        String consultationQuestionAnswersJson = objectMapper.writeValueAsString(consultationFormDto);

        mockMvc.perform(MockMvcRequestBuilders.post(POST_CONSULTATION_QUESTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consultationQuestionAnswersJson))
                .andExpect(status().isBadRequest());
    }
}
