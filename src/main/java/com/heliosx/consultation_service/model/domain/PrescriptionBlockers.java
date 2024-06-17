package com.heliosx.consultation_service.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class PrescriptionBlockers {
    Long questionId;
    String reason;
}
