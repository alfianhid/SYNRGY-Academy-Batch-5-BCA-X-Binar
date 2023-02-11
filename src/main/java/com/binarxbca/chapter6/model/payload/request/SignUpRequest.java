package com.binarxbca.chapter6.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank
    @Schema(example = "Alfian")
    @Column(name = "first_name")
    @Size(max = 40, message = "first name must be maximum 40 characters")
    private String firstName;

    @NotBlank
    @Schema(example = "Hidayatulloh")
    @Column(name = "last_name")
    @Size(max = 40, message = "last name must be maximum 40 characters")
    private String lastName;

    @NotBlank
    @Schema(example = "alfianhid")
    @Column(name = "username")
    @Size(max = 15, message = "username must be maximum 15 characters")
    private String username;

    @NotBlank
    @Schema(example = "alfianhid@gmail.com")
    @Size(max = 40, message = "email must be maximum 40 characters")
    @Column(name = "email")
    @Email
    private String email;

    @NotBlank
    @Schema(example = "1niP4s5W0rDS4yA")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 100, message = "password must be maximum 100 characters")
    @Column(name = "password")
    private String password;
}
