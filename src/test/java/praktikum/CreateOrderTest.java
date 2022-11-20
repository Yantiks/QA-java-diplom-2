package praktikum;

import com.dto.*;
import com.client.OrderClient;
import com.client.UserClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static com.dto.OrderGenerator.getRandomOrder;
import static com.dto.UserGenerator.getRandomUser;

public class CreateOrderTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private UserLogin userLogin;
    private Order order;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = getRandomUser();
        //create
        token = userClient.create(user)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");
        ;

        userLogin = LoginGenerator.from(user);
        orderClient = new OrderClient();
        order = getRandomOrder();
    }

    @After
    public void tearDown() {
        userClient.delete(token)
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .and()
                .body("success", Matchers.equalTo(true));
    }


    //Создание заказа: с авторизацией
    @Test
    @DisplayName("Order creation with authorization")
    public void orderCreationWithAuth() {
        token = userClient.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        orderClient.create(order, token)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .and()
                .body("order.number", Matchers.notNullValue());
    }

    //Создание заказа: без авторизации
    @Test
    @DisplayName("Order creation without authorization")
    public void orderCreationWithoutAuth() {
        orderClient.create(order)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .and()
                .body("order.number", Matchers.notNullValue());
    }


    //Создание заказа: без ингредиентов
    @Test
    @DisplayName("Order creation without ingredients")
    public void orderCreationWithoutIngredients() {
        token = userClient.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        order.setIngredients(null);
        orderClient.create(order, token)
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    //Создание заказа: с неверным хешем ингредиентов
    @Test
    @DisplayName("Order creation with wrong hash")
    public void orderCreationWithIncorrectHash() {
        token = userClient.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        List<String> incorrectHash = new ArrayList<>();
        incorrectHash.add(RandomStringUtils.randomAlphabetic(10));
        order.setIngredients(incorrectHash);
        orderClient.create(order, token)
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
