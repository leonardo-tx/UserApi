package com.at.userapi.controller;

import com.at.userapi.dto.UsuarioDTOInput;
import com.at.userapi.dto.UsuarioDTOOutput;
import com.at.userapi.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

import java.net.HttpURLConnection;
import java.util.Objects;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.put;

public final class UsuarioController {
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void respostasRequisicoes() {
        get("/usuarios", this::getAllEndpoint);
        get("/usuarios/:id", this::getByIdEndpoint);
        delete("/usuarios/:id", this::deleteEndpoint);
        post("/usuarios", this::addEndpoint);
        put("/usuarios", this::updateEndpoint);
    }

    private String getAllEndpoint(Request request, Response response) throws JsonProcessingException {
        response.type("application/json");
        response.status(HttpURLConnection.HTTP_OK);

        return objectMapper.writeValueAsString(usuarioService.listar());
    }

    private String getByIdEndpoint(Request request, Response response) throws JsonProcessingException {
        response.type("application/json");
        String idParameter = request.params("id");

        int id;
        try {
            id = Integer.parseInt(idParameter);
        } catch (NumberFormatException e) {
            response.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return getErrorMessageAsJson("O parâmetro de id é inválido.");
        }

        UsuarioDTOOutput usuarioOutput = usuarioService.buscar(id);
        if (usuarioOutput == null) {
            response.status(HttpURLConnection.HTTP_NOT_FOUND);
            return getErrorMessageAsJson("Não foi encontrado um usuário com o id dado.");
        }
        response.status(HttpURLConnection.HTTP_OK);
        return objectMapper.writeValueAsString(usuarioOutput);
    }

    private String deleteEndpoint(Request request, Response response) {
        response.type("application/json");
        String idParameter = request.params("id");

        int id;
        try {
            id = Integer.parseInt(idParameter);
        } catch (NumberFormatException e) {
            response.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return getErrorMessageAsJson("O parâmetro de id é inválido.");
        }

        if (usuarioService.excluir(id)) {
            response.status(HttpURLConnection.HTTP_OK);
            return "";
        }
        response.status(HttpURLConnection.HTTP_NOT_FOUND);
        return getErrorMessageAsJson("Não foi encontrado um usuário com o id dado.");
    }

    private String addEndpoint(Request request, Response response) {
        response.type("application/json");
        if (!Objects.equals(request.contentType(), "application/json")) {
            response.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return getErrorMessageAsJson("O Header-Content não consta como 'application/json'.");
        }

        try {
            UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);
            if (usuarioService.inserir(usuarioDTOInput)) {
                response.status(HttpURLConnection.HTTP_CREATED);
                return "";
            }
            response.status(HttpURLConnection.HTTP_CONFLICT);
            return getErrorMessageAsJson("Um usuário já existe com o mesmo id inserido, escolha outro.");
        } catch (Exception e) {
            response.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return getErrorMessageAsJson("O formulário se encontra inválido.");
        }
    }

    private String updateEndpoint(Request request, Response response) {
        response.type("application/json");
        if (!Objects.equals(request.contentType(), "application/json")) {
            response.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return getErrorMessageAsJson("O Header-Content não consta como 'application/json'.");
        }

        try {
            UsuarioDTOInput usuarioDTOInput = objectMapper.readValue(request.body(), UsuarioDTOInput.class);
            if (usuarioService.alterar(usuarioDTOInput)) {
                response.status(HttpURLConnection.HTTP_OK);
                return "";
            }
            response.status(HttpURLConnection.HTTP_NOT_FOUND);
            return getErrorMessageAsJson("Não foi possível encontrar um objeto com o mesmo id para ser atualizado.");
        } catch (Exception e) {
            response.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return getErrorMessageAsJson("O formulário se encontra inválido.");
        }
    }

    private String getErrorMessageAsJson(String errorMessage) {
        return "{\"error\":\"" + errorMessage + "\"}";
    }
}