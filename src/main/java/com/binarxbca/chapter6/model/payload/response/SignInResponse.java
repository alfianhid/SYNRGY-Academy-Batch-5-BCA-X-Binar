package com.binarxbca.chapter6.model.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class SignInResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public SignInResponse(String token, String type, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
