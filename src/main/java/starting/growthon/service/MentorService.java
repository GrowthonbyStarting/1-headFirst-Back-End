package starting.growthon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.entity.*;
import starting.growthon.repository.CompanyRepository;
import starting.growthon.repository.MentorInfoRepository;
import starting.growthon.repository.SubJobRepository;
import starting.growthon.repository.UserRepository;
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

    public MentorService(MentorInfoRepository mentorInfoRepository, UserRepository userRepository,
                         UserUtil userUtil,
                         CompanyRepository companyRepository,
                         SubJobRepository subJobRepository) {
        this.mentorInfoRepository = mentorInfoRepository;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
        this.companyRepository = companyRepository;
        this.subJobRepository = subJobRepository;
    }

    public MentorInfo changeRole() {
        User targetUser = userUtil.getLoggedInUser();
        targetUser.setRole("MENTOR");
        return createMentorInfo(targetUser);
    }

    private MentorInfo createMentorInfo(User targetUser) {
        if (mentorInfoRepository.findByMentorId(targetUser.getId()) == null) {
            MentorInfo mentorInfo = new MentorInfo("", 0, 0, targetUser);
            return mentorInfoRepository.save(mentorInfo);
        }
        return mentorInfoRepository.findByMentorId(targetUser.getId());
    }

    public List<MentorInfo> getMentors() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equals("MENTOR"))
                .map(mentor -> mentorInfoRepository.findByMentorId(mentor.getId()))
                .collect(Collectors.toList());
    }

    public List<MentorInfo> mentorSearch(String condition) {
        // 아예 condition 값이 비어 있는 경우는 없다고 가정 (프론트에서 막아야 함)
        List<MentorInfo> allMentors = mentorFindByCompany(condition);
        allMentors.addAll(mentorFindBySubjob(condition));
        return new ArrayList<>(new HashSet<>(allMentors));
    }

    private List<MentorInfo> mentorFindByCompany(String company) {
        List<Company> companies = companyRepository.findAllByNameContaining(company);

        if (companies.size() == 0 || company.isEmpty())
            return new ArrayList<>();

        List<MentorInfo> mentorInfos = new ArrayList<>();
        for (Company com : companies) {
            List<User> users = userRepository.findAllByCompanyId(com.getId());
            for (User user : users) {
                MentorInfo mentorInfo = mentorInfoRepository.findByMentorId(user.getId());
                mentorInfos.add(mentorInfo);
            }
        }
        return mentorInfos;
    }

    private List<MentorInfo> mentorFindBySubjob(String job) {
        List<SubJob> subJobList = subJobRepository.findAllByNameContaining(job);

        if (subJobList.size() == 0 || job.isEmpty())
            return new ArrayList<>();

        List<MentorInfo> mentorInfos = new ArrayList<>();
        for (SubJob subJob : subJobList) {
            List<User> users = userRepository.findAllBySubjobId(subJob.getId());
            for (User user : users) {
                MentorInfo mentorInfo = mentorInfoRepository.findByMentorId(user.getId());
                if (mentorInfo != null) {
                    mentorInfos.add(mentorInfo);
                }
            }
        }
        return mentorInfos;
    }
}
