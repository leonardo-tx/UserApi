import com.at.userapi.dto.UsuarioDTOInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.RandomUser;
import org.junit.Assert;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class UserApiTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testGetAll() throws IOException {
        HttpURLConnection connection = null;

        try {
            URL url = new URL("http://localhost:4567/usuarios");
            connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            Assert.assertEquals(HttpURLConnection.HTTP_OK, responseCode);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    @Test
    public void testAdd() throws IOException {
        UsuarioDTOInput dummyUser = getDummyUser();
        HttpURLConnection connection = null;

        try {
            URL url = new URL("http://localhost:4567/usuarios");
            connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonData =  objectMapper.writeValueAsString(dummyUser);
            connection.getOutputStream().write(jsonData.getBytes());

            int responseCode = connection.getResponseCode();
            Assert.assertEquals(HttpURLConnection.HTTP_CREATED, responseCode);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private UsuarioDTOInput getDummyUser() throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("https://randomuser.me/api/");
            connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) throw new IOException("Não foi possível acessar a API RandomUser.");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();

            String currentLine;
            while ((currentLine = reader.readLine()) != null) builder.append(currentLine);

            RandomUser randomUser = objectMapper.readValue(builder.toString(), RandomUser.class);
            return modelMapper.map(randomUser, UsuarioDTOInput.class);
        } finally {
            if (reader != null) reader.close();
            if (connection != null) connection.disconnect();
        }
    }
}