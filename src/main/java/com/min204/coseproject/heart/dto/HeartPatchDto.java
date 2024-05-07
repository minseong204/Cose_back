package com.min204.coseproject.heart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HeartPatchDto {
    @NotBlank
    private Long contentId;
    @NotBlank
    private String heartType;
}
