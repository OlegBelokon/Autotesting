package stepdefinitions;

import io.github.cdimascio.dotenv.Dotenv;

public class SecretLoader {
    private static final Dotenv dotenv = Dotenv.configure()
            .filename("secrets.env") // имя вашего файла
            .ignoreIfMissing()       // не падать, если файла нет (для CI)
            .load();

    public static String get(String key) {
        String value = dotenv.get(key);
        if (value == null) {
            // если переменной нет в .env, пробуем получить из системных переменных (Jenkins)
            value = System.getenv(key);
        }
        if (value == null) {
            throw new IllegalArgumentException("Secret not found: " + key);
        }
        return value;
    }
}