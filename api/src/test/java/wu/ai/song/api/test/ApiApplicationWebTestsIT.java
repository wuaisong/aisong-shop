package wu.ai.song.api.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationWebTestsIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        String response = restTemplate.getForObject("/hello/55", String.class);
        System.out.println("web test");
        Assertions.assertThat(response).contains("Hello");
    }

}
