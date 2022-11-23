package praktikum;

import com.client.OrderClient;
import com.client.UserClient;
import com.dto.LoginGenerator;
import com.dto.Order;
import com.dto.User;
import com.dto.UserLogin;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static com.dto.OrderGenerator.getRandomOrder;
import static com.dto.UserGenerator.getRandomUser;

public class GetOrderTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private UserLogin userLogin;
    private Order order;
    private String token;

    //Получение заказов конкретного пользователя: авторизованный пользователь
    @Test
    @DisplayName("Get orders with authorization")
    public void getOrdersWithAuth() {
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

        token = userClient.login(userLogin)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true))
                .extract()
                .path("accessToken");

        orderClient = new OrderClient();
        orderClient.get(token)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.equalTo(true));

        userClient.delete(token)
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .and()
                .body("success", Matchers.equalTo(true));
    }

    //Получение заказов конкретного пользователя: неавторизованный пользователь
    @Test
    @DisplayName("Get orders without authorization")
    public void getOrdersWithoutAuth() {
        orderClient = new OrderClient();
        orderClient.get()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", Matchers.equalTo(false))
                .and()
                .body("message", Matchers.equalTo("You should be authorised"));
    }
}
