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

    public String toEmailString() {
        StringBuilder sb = new StringBuilder();
        sb.append(day).append(": ");
        for (int i = 0; i < time.size(); i++) {
            sb.append(time.get(i));
            if (i < time.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
