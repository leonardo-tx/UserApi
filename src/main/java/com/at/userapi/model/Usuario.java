package com.at.userapi.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public final class Usuario {
    @NonNull
    private int id;
    @NonNull
    private String nome;
    @NonNull
    private String senha;
}