package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Random;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RandomUser {
    private final long id = new Random().nextInt();
    private String nome;
    private String senha;

    public void setResults(JsonNode node) {
        JsonNode firstResult = node.get(0);

        JsonNode nameNode = firstResult.get("name");
        nome = nameNode.get("first").asText() + ' ' + nameNode.get("last").asText();

        JsonNode loginNode = firstResult.get("login");
        senha = loginNode.get("password").asText();
    }
}