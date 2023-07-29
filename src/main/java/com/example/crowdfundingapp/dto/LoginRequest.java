package com.example.crowdfundingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Pattern(regexp = "^.+@.+\\..+$" , message = "Email is not valid")
    private String email;
    private String password;

}
