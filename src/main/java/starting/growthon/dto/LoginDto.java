package starting.growthon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    private String name;
    private Long uuid;

    public LoginDto(String name, Long uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
