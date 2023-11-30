package com.at.userapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UsuarioDTOInput {
    private int id;
    private String nome;
    private String senha;
}