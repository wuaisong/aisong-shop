package wu.ai.song.api.test.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String houseNo;
}
