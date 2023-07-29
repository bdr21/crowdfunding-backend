package com.example.crowdfundingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter @Setter
public class BasicResponse {
    private HttpStatus status;
    private String message;
}
