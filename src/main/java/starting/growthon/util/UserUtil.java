package starting.growthon.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import starting.growthon.entity.User;
import starting.growthon.exception.NotLoggedInException;
import starting.growthon.repository.UserRepository;

import java.util.Optional;

@Component
public class UserUtil {
    private final UserRepository userRepository;

    public UserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty())
            return null;
        return findUser.get();
    }
}
