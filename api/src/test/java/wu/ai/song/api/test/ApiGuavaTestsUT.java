package wu.ai.song.api.test;

import com.google.common.collect.Streams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wu.ai.song.api.ApiApplication;
import wu.ai.song.api.test.entity.Person;
import wu.ai.song.api.test.entity.PersonAddress;
import wu.ai.song.api.test.entity.PersonDTO;

import java.util.stream.Stream;

@SpringBootTest(classes = ApiApplication.class)
//不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@AutoConfigureMockMvc
@ActiveProfiles("ut")
@Transactional
public class ApiGuavaTestsUT {
    @Test
    public void test2() {
        Stream<Integer> s1 = Stream.of(1, 2, 3);
        Stream<Integer> s2 = Stream.of(4, 5, 6);
        Stream<Integer> s3 = Stream.of(7, 8, 9);
        Stream<Integer> s4 = Streams.concat(s1, s2, s3);
        Assertions.assertEquals(9, s4.count());
    }

    @Test
    public void test1() {
        Stream<Person> s1 = Stream.of(
                new Person(1, "John", "Smith"),
                new Person(2, "Tom", "Hamilton"),
                new Person(3, "Paul", "Walker")
        );
        Stream<PersonAddress> s2 = Stream.of(
                new PersonAddress(1, "London", "Street1", "100"),
                new PersonAddress(2, "Manchester", "Street1", "101"),
                new PersonAddress(3, "London", "Street2", "200")
        );
        Stream<PersonDTO> s3 = Streams.zip(s1, s2, (p, pa) -> PersonDTO.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .city(pa.getCity())
                .street(pa.getStreet())
                .houseNo(pa.getHouseNo()).build());
        s3.forEach(dto -> {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getFirstName());
            Assertions.assertNotNull(dto.getCity());
        });
    }
}
