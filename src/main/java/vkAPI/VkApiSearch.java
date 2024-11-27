package vkAPI;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

public class VkApiSearch {

    public static int unknownCityCount = 0; // Счётчик студентов, у которых город не определён

    public static String getVkToken() {
        Dotenv dotenv = Dotenv.load();
        return dotenv.get("TOKEN");
    }

    private static final String VK_TOKEN = getVkToken();

    public static String getCityFromStudent(String name) {
        try {
            // Кодируем имя для безопасной передачи через URL
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);

            // Формируем URL для поиска
            String apiUrl = String.format(
                    "https://api.vk.com/method/users.search?q=%s&fields=city&access_token=%s&v=5.131",
                    encodedName, VK_TOKEN
            );

            // Создаем HttpClient для отправки запроса
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Парсим JSON-ответ
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

            // Проверка, есть ли в ответе данные
            if (jsonResponse.has("response") &&
                    jsonResponse.getAsJsonObject("response").has("items")) {
                JsonArray itemsArray = jsonResponse
                        .getAsJsonObject("response")
                        .getAsJsonArray("items");

                // Проверка, есть ли результаты поиска
                if (!itemsArray.isEmpty()) {
                    JsonObject userInfo = itemsArray.get(0).getAsJsonObject(); // Берем первого найденного пользователя

                    // Извлекаем город
                    if (userInfo.has("city")) {
                        JsonObject cityObject = userInfo.getAsJsonObject("city");
                        return cityObject.get("title").getAsString();
                    } else {
                        unknownCityCount++;
                        return "город студента не указан";
                    }
                } else {
                    unknownCityCount++;
                    return "пользователь не найден";
                }
            } else {
                unknownCityCount++;
                return "Ошибка при получении данных";
            }

        } catch (Exception e) {
            unknownCityCount++;
            return "Ошибка при получении данных";
        }
    }
}
