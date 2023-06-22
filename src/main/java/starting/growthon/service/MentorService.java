package starting.growthon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.response.MentorInfoDto;
import starting.growthon.entity.*;
import starting.growthon.repository.*;
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

    public MentorService(MentorInfoRepository mentorInfoRepository, UserRepository userRepository,
                         UserUtil userUtil,
                         CompanyRepository companyRepository,
                         SubJobRepository subJobRepository,
                         FollowRepository followRepository) {
        this.mentorInfoRepository = mentorInfoRepository;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
        this.companyRepository = companyRepository;
        this.subJobRepository = subJobRepository;
        this.followRepository = followRepository;
    }

    public MentorInfo changeRole() {
        User targetUser = userUtil.getLoggedInUser();
        targetUser.setRole("MENTOR");
        return createMentorInfo(targetUser);
    }

    private MentorInfo createMentorInfo(User targetUser) {
        if (mentorInfoRepository.findByMentorId(targetUser.getId()).isEmpty()) {
            MentorInfo mentorInfo = new MentorInfo("", 0, 0, targetUser, null);
            return mentorInfoRepository.save(mentorInfo);
        }
        return mentorInfoRepository.findByMentorId(targetUser.getId()).get();
    }

    public List<MentorInfoDto> getMentors() {
        ArrayList<MentorInfoDto> allMentors = new ArrayList<>();
        List<User> mentors = userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("MENTOR")).collect(Collectors.toList());
        for (User mentor : mentors) {
            createMentorInfoDtoUsingMentor(allMentors, mentor);
        }
        return allMentors;
    }

    public List<MentorInfoDto> mentorSearch(String condition) {
        // 아예 condition 값이 비어 있는 경우는 없다고 가정 (프론트에서 막아야 함)
        List<MentorInfoDto> allMentors = new ArrayList<>();
        allMentors.addAll(mentorFindByCompany(condition));
        allMentors.addAll(mentorFindBySubjob(condition));
        return new ArrayList<>(new HashSet<>(allMentors));
    }

    private List<MentorInfoDto> mentorFindByCompany(String company) {
        List<Company> companies = companyRepository.findAllByNameContaining(company);

        if (companies.size() == 0 || company.isEmpty())
            return new ArrayList<>();

        ArrayList<MentorInfoDto> mentorInfos = new ArrayList<>();

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

    private List<MentorInfoDto> mentorFindBySubjob(String job) {
        List<SubJob> subJobList = subJobRepository.findAllByNameContaining(job);

        if (subJobList.size() == 0 || job.isEmpty())
            return new ArrayList<>();

        ArrayList<MentorInfoDto> mentorInfos = new ArrayList<>();
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

    private void createMentorInfoDtoUsingMentor(ArrayList<MentorInfoDto> allMentors, User mentor) {
        var mentorInfo = mentorInfoRepository.findByMentorId(mentor.getId()).get();
        var followers = followRepository.findAllByMentorId(mentor.getId());
        allMentors.add(
                MentorInfoDto.builder()
                        .mentor(mentor)
                        .content(mentorInfo.getContent())
                        .cost(mentorInfo.getCost())
                        .view(mentorInfo.getView())
                        .followers(followers.size())
                        .summary(mentorInfo.isSummary())
                        .univ(mentorInfo.getUniv().getName())
                        .build()
        );
    }
}
