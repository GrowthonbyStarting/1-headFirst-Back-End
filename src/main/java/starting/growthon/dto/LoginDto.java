package starting.growthon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String email;
    private String name;

    public LoginDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
