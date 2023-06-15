package starting.growthon.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import starting.growthon.entity.User;
import starting.growthon.exception.NotLoggedInException;
import starting.growthon.repository.UserRepository;

@Component
public class UserUtil {
    private final UserRepository userRepository;

    public UserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.findByName(name).orElseThrow(
                () -> new NotLoggedInException("로그인되지 않았습니다.")
        );
    }
}
