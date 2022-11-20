package praktikum;

import com.dto.*;
import com.client.UserClient;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.junit4.DisplayName;

import static com.dto.UserGenerator.getRandomUser;

@RunWith(Parameterized.class)
public class UserChangesTest {

    private UserClient user;
    private User randomUser;
    private UserLogin userLogin;
    private String token;

    private final String userName;
    private final String userEmail;
    private final String userPassword;

    public UserChangesTest(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    @Parameterized.Parameters(name = "userName={0}, userEmail = {1}, userPassword={2}")
    public static Object[][] params() {
        User randomUser = getRandomUser();
        return new Object[][]{
                {randomUser.getName(), null, null},
                {null, randomUser.getEmail(), null},
                {null, null, randomUser.getPassword()},
                {randomUser.getName(), randomUser.getEmail(), randomUser.getPassword()}
        };
    }


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
    }

    @After
    public void tearDown() {
        user.delete(token)
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    //Изменение данных пользователя: с авторизацией
    @Test
    @DisplayName("Change user data with authorization")
    public void userWithAuthTest() {
        userLogin = LoginGenerator.from(randomUser);
        token = user.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        if (userName != null) randomUser.setName(userName);
        if (userEmail != null) randomUser.setEmail(userEmail);
        if (userPassword != null) randomUser.setPassword(userPassword);

        user.change(randomUser, token)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true));

    }

    //Изменение данных пользователя: без авторизации
    @Test
    @DisplayName("Change user data without authorization")
    public void userWithoutAuthTest() {
        if (userName != null) randomUser.setName(userName);
        if (userEmail != null) randomUser.setEmail(userEmail);
        if (userPassword != null) randomUser.setPassword(userPassword);
        user.change(randomUser)
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", Matchers.equalTo("You should be authorised"));
    }
}
