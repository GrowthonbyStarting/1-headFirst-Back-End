package starting.growthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.MentorInfoRequestDto;
import starting.growthon.dto.response.MentorInfoResponseDto;
import starting.growthon.entity.*;
import starting.growthon.repository.*;
import starting.growthon.util.S3Uploader;
import starting.growthon.util.UserUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MentorService {

    private final MentorInfoRepository mentorInfoRepository;
    private final UserRepository userRepository;
    private final UserUtil userUtil;
    private final CompanyRepository companyRepository;
    private final SubJobRepository subJobRepository;
    private final FollowRepository followRepository;

    private final YearRepository yearRepository;

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private FileRepository fileRepository;
    private final JobRepository jobRepository;
    private final KeywordRepository keywordRepository;
    private final UserAndKeywordRepository userAndKeywordRepository;

    public MentorService(MentorInfoRepository mentorInfoRepository, UserRepository userRepository,
                         UserUtil userUtil,
                         CompanyRepository companyRepository,
                         SubJobRepository subJobRepository,
                         FollowRepository followRepository, YearRepository yearRepository,
                         JobRepository jobRepository,
                         KeywordRepository keywordRepository,
                         UserAndKeywordRepository userAndKeywordRepository) {
        this.mentorInfoRepository = mentorInfoRepository;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
        this.companyRepository = companyRepository;
        this.subJobRepository = subJobRepository;
        this.followRepository = followRepository;
        this.yearRepository = yearRepository;
        this.jobRepository = jobRepository;
        this.keywordRepository = keywordRepository;
        this.userAndKeywordRepository = userAndKeywordRepository;
    }

    public MentorInfo changeRole() {
        User targetUser = userUtil.getLoggedInUser();
        targetUser.setRole("MENTOR");
        return createMentorInfo(targetUser);
    }

    private MentorInfo createMentorInfo(User targetUser) {
        if (mentorInfoRepository.findByMentorId(targetUser.getId()) == null) {
            MentorInfo mentorInfo = new MentorInfo("", 0, 0, targetUser, null);
            return mentorInfoRepository.save(mentorInfo);
        }
        return mentorInfoRepository.findByMentorId(targetUser.getId());
    }

    public List<MentorInfoResponseDto> getMentors() {
        ArrayList<MentorInfoResponseDto> allMentors = new ArrayList<>();
        List<User> mentors = userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("MENTOR")).toList();
        for (User mentor : mentors) {
            createMentorInfoDtoUsingMentor(allMentors, mentor);
        }
        return allMentors;
    }

    public List<MentorInfoResponseDto> mentorSearch(String condition) {
        // 아예 condition 값이 비어 있는 경우는 없다고 가정 (프론트에서 막아야 함)
        List<MentorInfoResponseDto> allMentors = new ArrayList<>();
        allMentors.addAll(mentorFindByCompany(condition));
        allMentors.addAll(mentorFindBySubjob(condition));
        return new ArrayList<>(new HashSet<>(allMentors));
    }

    private List<MentorInfoResponseDto> mentorFindByCompany(String company) {
        List<Company> companies = companyRepository.findAllByNameContaining(company);

        if (companies.size() == 0 || company.isEmpty())
            return new ArrayList<>();

        ArrayList<MentorInfoResponseDto> mentorInfos = new ArrayList<>();

        for (Company com : companies) {
            List<User> companyMentors = userRepository.findAllByCompanyId(com.getId()).stream().filter(
                    user -> user.getRole().equals("MENTOR")
            ).toList();
            for (User mentor : companyMentors) {
                createMentorInfoDtoUsingMentor(mentorInfos, mentor);
            }
        }

        return mentorInfos;
    }

    private List<MentorInfoResponseDto> mentorFindBySubjob(String job) {
        List<SubJob> subJobList = subJobRepository.findAllByNameContaining(job);

        if (subJobList.size() == 0 || job.isEmpty())
            return new ArrayList<>();

        ArrayList<MentorInfoResponseDto> mentorInfos = new ArrayList<>();
        for (SubJob j : subJobList) {
            List<User> subJobMentors = userRepository.findAllBySubjobId(j.getId()).stream().filter(
                    user -> user.getRole().equals("MENTOR")
            ).toList();
            for (User mentor : subJobMentors) {
                createMentorInfoDtoUsingMentor(mentorInfos, mentor);
            }
        }
        return mentorInfos;
    }

    private void createMentorInfoDtoUsingMentor(ArrayList<MentorInfoResponseDto> allMentors, User mentor) {
        MentorInfo mentorInfo = null;
        if (mentorInfoRepository.findByMentorId(mentor.getId()) != null)
            mentorInfo = mentorInfoRepository.findByMentorId(mentor.getId());

        List<Follow> followers = followRepository.findAllByMentorId(mentor.getId());
        File img = fileRepository.findByTypeAndOwnerId("PROFILE", mentor.getId());
        String imgUrl = null;
        if (img != null) {
            imgUrl = img.getUrl();
        }
        allMentors.add(
                MentorInfoResponseDto.builder()
                        .mentor(mentor)
                        .content(mentorInfo.getContent())
                        .cost(mentorInfo.getCost())
                        .view(mentorInfo.getView())
                        .followers(followers.size())
                        .verified(mentorInfo.isVerified())
                        .keywords(userAndKeywordRepository.findAllByUserId(mentor.getId())
                                .stream().map(userAndKeyword -> {
                                    return userAndKeyword.getKeyword().getName();
                                }).collect(Collectors.toList()))
                        // 이것이 바로 연속 의존의 문제점..
                        .univ(mentorInfo.getUniv() == null ? null : mentorInfo.getUniv().getName())
                        .profile(imgUrl)
                        .build()
        );
    }

    public MentorInfoResponseDto infoUpload(MentorInfoRequestDto dto) {
        User mentor = userUtil.getLoggedInUser();

        checkIsMentor(mentor);

        MentorInfo mentorInfo = mentorInfoRepository.findByMentorId(mentor.getId());

        mentor.setName(dto.getName());
        mentor.setNickname(dto.getNickname());
        mentor.setCompany(companyRepository.findByName(dto.getCompany()));
        mentor.setYear(yearRepository.findByName(dto.getYear()));

        setJobAndSubJob(dto, mentor);

        saveKeywords(dto.getKeywords(), mentor);

        mentorInfo.setContent(dto.getContent());
        mentorInfo.setTitle(dto.getTitle());

        File img = fileRepository.findByTypeAndOwnerId("PROFILE", mentor.getId());
        String imgUrl = null;
        if (img != null) {
            imgUrl = img.getUrl();
        }

        List<Follow> followers = followRepository.findAllByMentorId(mentor.getId());

        return MentorInfoResponseDto.builder()
                .mentor(mentor)
                .content(dto.getContent())
                .cost(mentorInfo.getCost())
                .profile(imgUrl)
                .followers(followers.size())
                .verified(mentorInfo.isVerified())
                .univ(mentorInfo.getUniv() == null ? null : mentorInfo.getUniv().getName())
                .keywords(userAndKeywordRepository.findAllByUserId(mentor.getId())
                        .stream().map(userAndKeyword -> {
                            return userAndKeyword.getKeyword().getName();
                        }).collect(Collectors.toList()))
                .view(mentorInfo.getView())
                .build();
    }

    private void saveKeywords(List<String> keywords, User mentor) {
        keywords.forEach(keyword -> {
            Keyword validKeyword = keywordRepository.findByName(keyword);
            if (validKeyword != null)
                userAndKeywordRepository.save(new UserAndKeyword(mentor, validKeyword));
        });
    }

    private void checkIsMentor(User mentor) {
        MentorInfo mentorInfo = mentorInfoRepository.findByMentorId(mentor.getId());
        if (mentorInfo == null)
            throw new IllegalStateException("멘토가 아닙니다.");
    }

    private void setJobAndSubJob(MentorInfoRequestDto dto, User mentor) {
        Job job = jobRepository.findByTitle(dto.getJob());
        SubJob subJob = subJobRepository.findByName(dto.getSubjob());
        List<SubJob> possibleSubjob = subJobRepository.findAllByJobId(job.getId());
        if (possibleSubjob.contains(subJob))
            mentor.setSubjob(subJobRepository.findByName(dto.getSubjob()));
    }
}
