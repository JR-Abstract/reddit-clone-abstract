package ua.com.javarush.oleksandr.reddit.redditcloneabstract.testcontainer;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public final class PostgresqlTestContainer extends PostgreSQLContainer<PostgresqlTestContainer> {

    private static final String IMAGE_VERSION = "postgres:16.2";
    private static PostgresqlTestContainer container;

    private PostgresqlTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresqlTestContainer getInstance() {
        if (container == null) {
            container = new PostgresqlTestContainer();
        }
        return container;
    }
}
