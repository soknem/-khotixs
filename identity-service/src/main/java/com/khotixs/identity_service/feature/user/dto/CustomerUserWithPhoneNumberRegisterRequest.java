package com.khotixs.identity_service.feature.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CustomerUserWithPhoneNumberRegisterRequest (
        @NotEmpty(message = "Username is required")
        @Size(min = 5, message = "Username must be at least 5 characters long")
        @Size(max = 32, message = "Username can not be longer than 32 characters")
        @Email(message = "Email must be valid")
        String phoneNumber,

        @NotEmpty(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 5 characters long")
        @Size(max = 32, message = "Password can not be longer than 32 characters")
        String password,


        @NotEmpty(message = "Password confirmation is required")
        @Size(min = 6, message = "Confirmation password must be at least 6 characters long")
        @Size(max = 32, message = "Confirmation password can not be longer than 32 characters")
        String confirmedPassword,

        @Size(max = 64, message = "Family name can not be longer than 64 characters")
        @NotEmpty(message = "Family name is required")
        String familyName,

        @Size(max = 64, message = "Given name can not be longer than 64 characters")
        @NotEmpty(message = "Given name is required")
        String givenName,

        @NotEmpty(message = "Accepting Terms and Conditions is required")
        @Size(min = 4, max = 5, message = "Value must be either true or false")
        String acceptTerms

){
}
