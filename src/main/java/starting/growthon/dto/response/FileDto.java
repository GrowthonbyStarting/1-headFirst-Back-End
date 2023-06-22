package starting.growthon.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
    private String owner;
    private String url;
    private String type;

    public FileDto(String owner, String url, String type) {
        this.owner = owner;
        this.url = url;
        this.type = type;
    }
}
