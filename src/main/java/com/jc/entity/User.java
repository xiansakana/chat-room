package com.jc.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    private Integer id;
    @Pattern(regexp = "^[a-zA-Z0-9)]{1,16}$")
    @NotNull
    private String username;
    @Pattern(regexp = "^\\S{1,16}$")
    private String password;
}
