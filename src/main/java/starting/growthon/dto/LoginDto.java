package starting.growthon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String name;
    private Long uuid;

    public LoginDto(String name, Long uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
