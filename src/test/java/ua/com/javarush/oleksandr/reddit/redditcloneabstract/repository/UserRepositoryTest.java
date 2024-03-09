package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.testcontainer.PostgresqlTestContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        UserRepository.class}))
@ActiveProfiles({"tc", "tc-auto"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {
                "/sql/schema.sql",
                "/sql/dataUsers.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@DisplayName("UserRepositoryTest")
class UserRepositoryTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = PostgresqlTestContainer.getInstance();

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("findByName method should return User if User with given name is existed")
    void findByUserName_shouldReturnUser_whenThereIsSomeSUserInTableWithEnteredUserName() {
        String expectedUserName = "Michael";
        assertEquals(expectedUserName, userRepository.findByUsername(expectedUserName).get().getUsername());
    }
}
