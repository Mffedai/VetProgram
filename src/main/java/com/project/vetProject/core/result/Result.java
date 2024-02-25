package com.project.vetProject.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result {
    private boolean status;
    private String message;
    private String code;

}
