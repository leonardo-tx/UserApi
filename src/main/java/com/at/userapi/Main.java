package com.at.userapi;

import com.at.userapi.controller.UsuarioController;
import com.at.userapi.service.UsuarioService;

public class Main {
    public static void main(String[] args) {
        UsuarioService usuarioServiceSingleton = new UsuarioService();
        UsuarioController usuarioController = new UsuarioController(usuarioServiceSingleton);

        usuarioController.respostasRequisicoes();
    }
}