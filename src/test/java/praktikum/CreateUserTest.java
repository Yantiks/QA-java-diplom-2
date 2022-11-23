package praktikum;

import com.dto.LoginGenerator;
import com.dto.User;
import com.dto.UserLogin;
import com.client.UserClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static com.dto.UserGenerator.getRandomUser;

public class CreateUserTest {

    private UserClient user;
    private User randomUser;
    private UserLogin userLogin;
    private String token;

    @Before
    public void setUp() {
        user = new UserClient();
        randomUser = getRandomUser();
        token = null;
    }

    @After
    public void tearDown() {
        if (token != null) {
            user.delete(token)
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED)
                    .and()
                    .body("success", Matchers.equalTo(true));
        }
    }

    //создать уникального пользователя;
    @Test
    @DisplayName("Create unique user")
    public void createUniqueUserTest() {
        //create
        token = user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        //login
        userLogin = LoginGenerator.from(randomUser);


        user.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true)).log().all();

    }

    //создать пользователя, который уже зарегистрирован;
    @Test
    @DisplayName("Create non-unique user")
    public void identicalLoginTest() {
        //создаём пользователя
        token = user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");
        ;

        randomUser.setPassword(RandomStringUtils.randomAlphabetic(9));
        randomUser.setName(RandomStringUtils.randomAlphabetic(9));
        user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", Matchers.equalTo("User already exists"));


    }

    //создать пользователя и не заполнить одно из обязательных полей (не передаём name)
    @Test
    @DisplayName("Create a user without name")
    public void emptyNameTest() {
        randomUser.setName(null);

        user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    //создать пользователя и не заполнить одно из обязательных полей (не передаём email)
    @Test
    @DisplayName("Create a user without email")
    public void emptyEmailTest() {
        randomUser.setEmail(null);

        user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    //создать пользователя и не заполнить одно из обязательных полей (не передаём password)
    @Test
    @DisplayName("Create a user without password")
    public void emptyPasswordTest() {
        randomUser.setPassword(null);

        user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

}
