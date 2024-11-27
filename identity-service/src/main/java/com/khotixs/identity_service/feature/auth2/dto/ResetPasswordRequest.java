package com.khotixs.identity_service.feature.auth2.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

		String username

) {
}