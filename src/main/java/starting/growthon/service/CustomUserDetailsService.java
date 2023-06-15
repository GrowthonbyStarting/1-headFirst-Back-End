package starting.growthon.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.entity.User;
import starting.growthon.exception.TargetNotFoundException;
import starting.growthon.repository.UserRepository;

import java.util.ArrayList;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User existUser = userRepository.findByName(name)
                .orElseThrow(() -> new TargetNotFoundException("해당 유저가 없습니다."));
        return createUser(name);
    }

    private org.springframework.security.core.userdetails.User createUser(String name) {
        return new org.springframework.security.core.userdetails.User(name,
                null,
                new ArrayList<>());
    }
}
