package com.binarxbca.chapter6.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignInRequest {
    @NotBlank
    @Schema(example = "alfianhid")
    @Column(name = "username")
    @Size(max = 15, message = "username must be maximum 15 characters")
    private String username;

    @NotBlank
    @Schema(example = "1niP4s5W0rDS4yA")
    @Size(max = 100, message = "password must be maximum 100 characters")
    @Column(name = "password")
    private String password;
}
