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
import starting.growthon.dto.response.UserInfoDto;
import starting.growthon.entity.MentorInfo;
import starting.growthon.entity.User;
import starting.growthon.jwt.JwtFilter;
import starting.growthon.jwt.TokenProvider;
import starting.growthon.repository.*;
import starting.growthon.util.UserUtil;

import java.util.Collections;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final MentorInfoRepository mentorInfoRepository;
    private final UserAndUniversityRepository userAndUniversityRepository;
    private final UserAndMajorRepository userAndMajorRepository;
    private final UserAndCompanyRepository userAndCompanyRepository;
    private final UserAndJobRepository userAndJobRepository;
    private final TokenProvider tokenProvider;
    private final UserUtil userUtil;

    public UserService(UserRepository userRepository, MentorInfoRepository mentorInfoRepository,
                       UserAndUniversityRepository userAndUniversityRepository,
                       UserAndMajorRepository userAndMajorRepository,
                       UserAndCompanyRepository userAndCompanyRepository, UserAndJobRepository userAndJobRepository,
                       TokenProvider tokenProvider, UserUtil userUtil) {
        this.userRepository = userRepository;
        this.mentorInfoRepository = mentorInfoRepository;
        this.userAndUniversityRepository = userAndUniversityRepository;
        this.userAndMajorRepository = userAndMajorRepository;
        this.userAndCompanyRepository = userAndCompanyRepository;
        this.userAndJobRepository = userAndJobRepository;
        this.tokenProvider = tokenProvider;
        this.userUtil = userUtil;
    }

    // 소셜 로그인에 따라 회원가입 로직은 삭제

    public ResponseEntity<TokenDto> login(LoginDto loginDto) {

        // 고유 식별자로 유저를 판별한 다음 유저가 없으면 생성해줘야 함
        User user = userRepository.findByUuid(loginDto.getUuid()).orElseGet(
                () -> createNewUser(loginDto.getName(), loginDto.getUuid()));

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getName(),null,
                Collections.singleton(simpleGrantedAuthority));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    public User changeRole() {
        User targetUser = userUtil.getLoggedInUser();
        targetUser.setRole("MENTOR");
        createMentorInfo(targetUser);
        return targetUser;
    }

    // 유저 신규 생성
    private User createNewUser(String name, Long uuid) {
        return userRepository.save(new User(name, uuid));
    }

    public UserInfoDto profile() {
        User targetUser = userUtil.getLoggedInUser();
        UserInfoDto newUserInfoDto = new UserInfoDto();
        newUserInfoDto.setUser(targetUser);
        newUserInfoDto.setUniversities(userAndUniversityRepository.findAllByUserId(targetUser.getId()));
        newUserInfoDto.setMajors(userAndMajorRepository.findAllByUserId(targetUser.getId()));
        newUserInfoDto.setCompanies(userAndCompanyRepository.findAllByUserId(targetUser.getId()));
        newUserInfoDto.setJobs(userAndJobRepository.findAllByUserId(targetUser.getId()));
        return newUserInfoDto;
    }

    private void createMentorInfo(User user) {
        MentorInfo mentorInfo = new MentorInfo(0L, "", false, user);
        user.setMentorInfo(mentorInfoRepository.save(mentorInfo));
    }
}
