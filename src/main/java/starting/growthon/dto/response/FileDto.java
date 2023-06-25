package starting.growthon.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
    private Long owner;
    private String url;
    private String type;

    public FileDto(Long owner, String url, String type) {
        this.owner = owner;
        this.url = url;
        this.type = type;
    }
}
