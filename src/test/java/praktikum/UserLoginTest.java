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

public class UserLoginTest {
    private UserClient user;
    private User randomUser;
    private UserLogin userLogin;
    private String token;

    @Before
    public void setUp() {
        user = new UserClient();
        randomUser = getRandomUser();
        //create
        token = user.create(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        userLogin = LoginGenerator.from(randomUser);
    }

    @After
    public void tearDown() {
        user.delete(token)
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    //логин под существующим пользователем
    @Test
    @DisplayName("Login with existent user")
    public void successLoginTest() {
        user.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true)).log().all();
    }

    //логин с неверным email
    @Test
    @DisplayName("Login with wrong email")
    public void successWrongLoginTest() {
        userLogin.setEmail(RandomStringUtils.randomAlphabetic(14));
        user.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect")).log().all();
    }

    //логин с неверным паролем
    @Test
    @DisplayName("Login with wrong password")
    public void successWrongPasswordTest() {
        userLogin.setPassword(RandomStringUtils.randomAlphabetic(14));
        user.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", Matchers.equalTo("email or password are incorrect")).log().all();
    }
}
