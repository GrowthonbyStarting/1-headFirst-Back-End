package starting.growthon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDto {
    private String day;
    private List<String> time;

    public ScheduleDto(String day, List<String> time) {
        this.day = day;
        this.time = time;
    }
}
