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
import starting.growthon.entity.User;
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

    // 소셜 로그인에 따라 회원가입 로직은 삭제

    public ResponseEntity<TokenDto> login(LoginDto loginDto) {

        // 이메일로 유저를 판별한 다음 유저가 없으면 생성해줘야 함
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseGet(
                () -> createNewUser(loginDto.getEmail(), loginDto.getName()));

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getName(),null,
                Collections.singleton(simpleGrantedAuthority));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    // 유저 신규 생성
    private User createNewUser(String email, String name) {
        return userRepository.save(new User(email, name));
    }

    public User profile() {
        return userUtil.getLoggedInUser();
    }

}