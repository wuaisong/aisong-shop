package wu.ai.song.api.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonAddress {
    private Integer id;
    private String city;
    private String street;
    private String houseNo;
}
