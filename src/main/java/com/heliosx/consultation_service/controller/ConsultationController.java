package com.heliosx.consultation_service.controller;

import com.heliosx.consultation_service.model.domain.PrescriptionBlockers;
import com.heliosx.consultation_service.model.dto.request.ConsultationFormDto;
import com.heliosx.consultation_service.model.dto.response.QuestionDto;
import com.heliosx.consultation_service.model.domain.Question;
import com.heliosx.consultation_service.service.ConsultationQuestionsService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/consultation-api")
public class ConsultationController {

    ConsultationQuestionsService consultationQuestionsService;

    ModelMapper modelMapper;

    @GetMapping("/{medicalConditionId}")
    public ResponseEntity<List<QuestionDto>> getQuestions(@PathVariable Long medicalConditionId) {
        List<Question> questions = consultationQuestionsService.getConsultationQuestions(medicalConditionId);
        List<QuestionDto> questionDtos = questions.stream()
                .map(question -> modelMapper.map(question, QuestionDto.class))
                .toList();

        return new ResponseEntity<>(questionDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Set<PrescriptionBlockers>> submitQuestions(@Valid @RequestBody ConsultationFormDto consultationFormDto) {
        Set<PrescriptionBlockers> prescriptionBlockers = consultationQuestionsService.assessConsultationQuestions(consultationFormDto);
        return new ResponseEntity<>(prescriptionBlockers, HttpStatus.OK);
    }
}
