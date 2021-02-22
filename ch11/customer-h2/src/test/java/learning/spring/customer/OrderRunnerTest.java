package learning.spring.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.spring.customer.model.NewOrderForm;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class OrderRunnerTest {
    private static MockWebServer webServer;
    private OrderRunner runner;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        webServer.shutdown();
    }

    @BeforeEach
    void setUpBeforeEach() {
        runner = new OrderRunner();
        runner.setRestTemplate(new RestTemplate());
        runner.setBinarytea("http://localhost:" + webServer.getPort());
    }

    @Test
    void testCallForEntity() throws Exception {
        // 构造应答
        String body = "{\"id\":1, \"status\":\"ORDERED\"}";
        MockResponse response = new MockResponse()
                .setResponseCode(HttpStatus.CREATED.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body);
        webServer.enqueue(response);

        ResponseEntity<String> entity = runner.callForEntity();
        // 验证响应
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_VALUE,
                entity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        assertEquals(body, entity.getBody());

        // 验证请求
        RecordedRequest request = webServer.takeRequest();
        assertEquals("/order", request.getPath());

        NewOrderForm form = objectMapper.readValue(request.getBody().readUtf8(),
                NewOrderForm.class);
        assertLinesMatch(Arrays.asList("1"), form.getItemIdList());
        assertEquals(90, form.getDiscount());
    }
}