package wu.ai.song.api.test;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest(classes = ApiApplication.class)
//不启动服务器,使用mockMvc进行测试http请求。启动了完整的Spring应用程序上下文，但没有启动服务器
@AutoConfigureMockMvc
@ActiveProfiles("ut")
@Transactional
public class ApiJoolTestsUT {
    @Test
    public void test6() {
        Seq<Person> s1 = Seq.of(
                new Person(1, "John", "Smith", 35),
                new Person(2, "Tom", "Hamilton", 45),
                new Person(3, "Paul", "Walker", 20)
        );
        Optional<Integer> sum = s1.sum(Person::getAge);
        Assertions.assertEquals(100, sum.get());
    }

    @Test
    public void test5() {
        Seq<PersonDTO> s1 = Seq.of(
                PersonDTO.builder().id(1).firstName("John").lastName("Smith").city("London").street("Street1").houseNo("100").build(),
                PersonDTO.builder().id(2).firstName("Tom").lastName("Hamilton").city("Manchester").street("Street1").houseNo("101").build(),
                PersonDTO.builder().id(3).firstName("Paul").lastName("Walker").city("London").street("Street2").houseNo("200").build(),
                PersonDTO.builder().id(4).firstName("Joan").lastName("Collins").city("Manchester").street("Street2").houseNo("201").build());
        Tuple2<Seq<PersonDTO>, Seq<PersonDTO>> t = s1.partition(dto -> dto.getStreet().equals("Street1"));
        Assertions.assertTrue(t.v1.count() == 2);
        Assertions.assertTrue(t.v2.count() == 2);
    }

    @Test
    public void test4() {
        Seq<Integer> s1 = Seq.of(1, 2, 3);
        Seq<Integer> s2 = Seq.of(4, 5, 6);
        Seq<Integer> s3 = Seq.of(7, 8, 9);
        Seq<Integer> s4 = Seq.concat(s1, s2, s3);
        Assertions.assertEquals(9, s4.count());
    }

    @Test
    public void test3() {
        Seq<PersonDTO> s1 = Seq.of(
                PersonDTO.builder().id(1).firstName("John").lastName("Smith").city("London").street("Street1").houseNo("100").build(),
                PersonDTO.builder().id(2).firstName("Tom").lastName("Hamilton").city("Manchester").street("Street1").houseNo("101").build(),
                PersonDTO.builder().id(3).firstName("Paul").lastName("Walker").city("London").street("Street2").houseNo("200").build(),
                PersonDTO.builder().id(4).firstName("Joan").lastName("Collins").city("Manchester").street("Street2").houseNo("201").build()
        );
        Map<String, List<PersonDTO>> m = s1.groupBy(PersonDTO::getCity);
        Assertions.assertNotNull(m.get("London"));
        Assertions.assertTrue(m.get("London").size() == 2);
        Assertions.assertNotNull(m.get("Manchester"));
        Assertions.assertTrue(m.get("Manchester").size() == 2);
    }

    @Test
    public void test2() {
        Seq<Person> s1 = Seq.of(
                new Person(1, "John", "Smith"),
                new Person(2, "Tom", "Hamilton"),
                new Person(3, "Paul", "Walker"));
        Seq<PersonAddress> s2 = Seq.of(
                new PersonAddress(2, "London", "Street1", "100"),
                new PersonAddress(3, "Manchester", "Street1", "101"),
                new PersonAddress(1, "London", "Street2", "200"));
        Seq<PersonDTO> s3 = s1.innerJoin(s2, (p, pa) -> p.getId().equals(pa.getId())).map(t -> PersonDTO.builder()
                .id(t.v1.getId())
                .firstName(t.v1.getFirstName())
                .lastName(t.v1.getLastName())
                .city(t.v2.getCity())
                .street(t.v2.getStreet())
                .houseNo(t.v2.getHouseNo()).build());
        s3.forEach(dto -> {
            Assertions.assertNotNull(dto.getId());
            Assertions.assertNotNull(dto.getFirstName());
            Assertions.assertNotNull(dto.getCity());
        });
    }

    @Test
    public void test1() {
        Seq<Person> s1 = Seq.of(
                new Person(1, "John", "Smith"),
                new Person(2, "Tom", "Hamilton"),
                new Person(3, "Paul", "Walker"));
        Seq<PersonAddress> s2 = Seq.of(
                new PersonAddress(1, "London", "Street1", "100"),
                new PersonAddress(2, "Manchester", "Street1", "101"),
                new PersonAddress(3, "London", "Street2", "200"));
        Seq<PersonDTO> s3 = s1.zip(s2, (p, pa) -> PersonDTO.builder()
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
