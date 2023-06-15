package starting.growthon.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.LoginDto;
import starting.growthon.dto.TokenDto;
import starting.growthon.dto.UserDto;
import starting.growthon.entity.User;
import starting.growthon.exception.AlreadyExistUserException;
import starting.growthon.exception.LoginException;
import starting.growthon.exception.NotQualifiedDtoException;
import starting.growthon.exception.TargetNotFoundException;
import starting.growthon.jwt.JwtFilter;
import starting.growthon.jwt.TokenProvider;
import starting.growthon.repository.UserRepository;
import starting.growthon.util.UserUtil;

import java.util.Collections;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final UserUtil userUtil;

    public UserService(UserRepository userRepository, TokenProvider tokenProvider, UserUtil userUtil) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.userUtil = userUtil;
    }

    public User signup(UserDto userDto) {

        if (userDto.getName() == null || userDto.getEmail() == null)
            throw new NotQualifiedDtoException("name 또는 email이 비어있습니다.");

        if (userRepository.findByName(userDto.getName()).isPresent())
            throw new AlreadyExistUserException("이미 가입되어 있는 유저입니다.");

        User newUser = new User(userDto.getEmail(), userDto.getName());

        return userRepository.save(newUser);
    }

    public ResponseEntity<TokenDto> login(LoginDto loginDto) {

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getName(),null,
                Collections.singleton(simpleGrantedAuthority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByName(loginDto.getName())
                .orElseThrow(() -> new TargetNotFoundException("없는 유저입니다."));

        if (!user.getEmail().equals(loginDto.getEmail())) {
            throw new LoginException("이메일이 일치하지 않습니다.");
        }

        String jwt = tokenProvider.createToken(authentication, user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    public User profile() {
        return userUtil.getLoggedInUser();
    }

}
