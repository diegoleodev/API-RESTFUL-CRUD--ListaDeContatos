package com.example.contacts.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRecordDto(@NotBlank String name, @NotBlank String phone, @Email String email) {
}
