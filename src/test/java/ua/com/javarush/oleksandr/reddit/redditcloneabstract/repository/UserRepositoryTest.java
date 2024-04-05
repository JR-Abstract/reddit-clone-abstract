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
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.testcontainer.PostgresqlTestContainer;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles({"tc", "tc-auto"})
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        UserRepository.class}))
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
    @DisplayName("findByName method should return User if User with given name exists")
    void findByUserName_shouldReturnUser_whenThereIsSomeSUserInTableWithEnteredUserName() {
        String userName = "Michael";
        ZonedDateTime createdDate = ZonedDateTime.of(LocalDate
                .parse("2016-06-22").atStartOfDay(), ZoneOffset.ofHours(0));

        User actualUser = User.with()
                .userId(1L)
                .created_at(createdDate)
                .email("michael.thomas@gmail.com")
                .enabled(true)
                .password("michael1234")
                .username(userName)
                .build();

        User expectedUser = userRepository.findByUsername(userName).get();

        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("findByName method should return empty Optional if User with given name doesn't exist")
    void findByName_shouldReturnEmptyOptional_whenThereIsNotAnyUserInTableWithEnteredName() {

        String fakeName = "fakeName";

        assertTrue(userRepository.findByUsername(fakeName).isEmpty());
    }
}
