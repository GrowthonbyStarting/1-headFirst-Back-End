package starting.growthon.dto.response;

import lombok.Getter;
import lombok.Setter;
import starting.growthon.entity.*;

import java.util.List;

@Getter
@Setter
public class UserInfoDto {
    User user;
    List<UserAndUniversity> universities;
    List<UserAndMajor> majors;
    List<UserAndCompany> companies;
    List<UserAndJob> jobs;
}
