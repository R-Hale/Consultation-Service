package com.heliosx.consultation_service.service;

import com.heliosx.consultation_service.config.ConditionStrategy;
import com.heliosx.consultation_service.exceptions.InvalidConditionException;
import com.heliosx.consultation_service.exceptions.MissingQuestionAnswerException;
import com.heliosx.consultation_service.model.domain.PrescriptionBlockers;
import com.heliosx.consultation_service.model.dto.request.ConsultationFormDto;
import com.heliosx.consultation_service.model.dto.request.QuestionAnswerDto;
import com.heliosx.consultation_service.model.domain.Question;
import com.heliosx.consultation_service.service.validationStrategies.QuestionValidationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.heliosx.consultation_service.constants.constants.*;

@AllArgsConstructor
@Service
public class ConsultationQuestionsService {

    private static Map<ConditionStrategy, QuestionValidationStrategy> questionValidationStrategies = new HashMap<>();

    ConsultationQuestionsCache consultationQuestionsCache;

    public static void addQuestionValidationStrategy(ConditionStrategy conditionStrategy, QuestionValidationStrategy questionValidationStrategy) {
        questionValidationStrategies.put(conditionStrategy, questionValidationStrategy);
    }

    public List<Question> getConsultationQuestions(Long medicalConditionId) {
        return consultationQuestionsCache.getQuestions(medicalConditionId)
                .orElseThrow(() -> new InvalidConditionException(UNKNOWN_CONDITION_MSG));
    }

    public Set<PrescriptionBlockers> assessConsultationQuestions(ConsultationFormDto consultationFormDto) {
        Map<Long, QuestionAnswerDto> consultationQuestionsAnswered = checkAllQuestionsAnswered(consultationFormDto.getMedicalConditionId(), consultationFormDto.getQuestionAnswers());

        String conditionName = consultationQuestionsCache.getConditionNames(consultationFormDto.getMedicalConditionId())
                .orElseThrow(() -> new InvalidConditionException(UNKNOWN_CONDITION_MSG));

        //TODO - Save consultation questions to the DB for a doctor to review

        return questionValidationStrategies.get(ConditionStrategy.valueOf(conditionName)).validate(consultationQuestionsAnswered);
    }

    private Map<Long, QuestionAnswerDto> checkAllQuestionsAnswered(Long medicalConditionId, List<QuestionAnswerDto> questionAnswers) {
        List<Question> consultationQuestions = consultationQuestionsCache.getQuestions(medicalConditionId)
                .orElseThrow(() -> new InvalidConditionException(UNKNOWN_CONDITION_MSG));

        Map<Long, QuestionAnswerDto> consultationQuestionsAnswered = new HashMap();

        for (Question consultationQuestion : consultationQuestions) {
            QuestionAnswerDto questionAnswerDto = questionAnswers
                    .stream()
                    .filter(questionAnswer -> questionAnswer.getQuestionId() == consultationQuestion.getId())
                    .findFirst()
                    .orElseThrow(() -> new MissingQuestionAnswerException(MISSING_CONSULTATION_QUESTION_MSG));
            if(consultationQuestionsAnswered.put(questionAnswerDto.getQuestionId(), questionAnswerDto) != null){
                throw new InvalidConditionException(UNABLE_TO_PROCESS_QUESTIONS_MSG);
            }
        }
        return consultationQuestionsAnswered;
        //TEST OPTION SELECTED IS PRESENT
    }
}
