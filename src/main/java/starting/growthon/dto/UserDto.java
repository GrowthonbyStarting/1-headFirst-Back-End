package starting.growthon.dto;

import lombok.Getter;

@Getter
public class UserDto {
    private String email;
    private String name;

    public UserDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}