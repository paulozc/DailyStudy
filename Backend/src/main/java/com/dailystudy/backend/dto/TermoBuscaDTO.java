package com.dailystudy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TermoBuscaDTO(@NotBlank @Size(max = 100) String termo) {
}
