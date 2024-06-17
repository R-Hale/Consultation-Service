package com.heliosx.consultation_service.service;

import com.heliosx.consultation_service.model.domain.Question;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ConfigurationProperties(prefix = "conditions")
@Configuration
@Setter
@Getter
public class ConsultationQuestionsCache {
    private Map<Long, List<Question>> conditionQuestions = new HashMap<>();
    private Map<Long, String> conditionNames;

    public Optional<List<Question>> getQuestions(Long medicalConditionId) {
        return Optional.ofNullable(conditionQuestions.get(medicalConditionId));
    }

    public Optional<String> getConditionNames(Long medicalConditionId) {
        return Optional.ofNullable(conditionNames.get(medicalConditionId));
    }
}
