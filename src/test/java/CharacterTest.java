import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.example.domain.Character;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class CharacterTest {
    private HttpResponse getHttpResponse(String url)  {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }
    @Test
    public void shouldReturnStatusCode200() throws IOException, InterruptedException {
        HttpResponse <String> response = getHttpResponse("https://rickandmortyapi.com/api/character/40");
        System.out.println("Response code: " + response.statusCode());
        final int expectedCode = 200;
        Assertions.assertEquals(expectedCode, response.statusCode());
    }
    @Test
    public void shouldReturnStatusCode404() throws IOException, InterruptedException {
        HttpResponse <String> response = getHttpResponse("https://rickandmortyapi.com/api/character/9999");
        System.out.println("Response code: " + response.statusCode());
        final int expectedCode = 404;
        Assertions.assertEquals(expectedCode, response.statusCode());
    }

    @Test
    public void shouldReturnCorrectJSONBody() throws IOException, InterruptedException {
        final String expected = Files.readString(Path.of("src/test/resources/character.json"));
        HttpResponse <String> response = getHttpResponse("https://rickandmortyapi.com/api/character/40");
        System.out.println("Response code: " + response.statusCode());
        Assertions.assertEquals(expected, response.body());
    }
    @Test
    public void contentTestWithObjects() throws JsonProcessingException {
        Character expected = Character.builder()
                .id(1)
                .name("Rick Sanchez")
                .gender("Male")
                .species("Human")
                .status("Alive")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse <String> response = getHttpResponse("https://rickandmortyapi.com/api/character/1");
        Character actual = mapper.readValue(response.body(), Character.class);
        Assertions.assertEquals(expected, actual);
    }
}
