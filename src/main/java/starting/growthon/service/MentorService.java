package starting.growthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.MentorInfoRequestDto;
import starting.growthon.dto.ScheduleDto;
import starting.growthon.dto.response.MentorInfoResponseDto;
import starting.growthon.entity.*;
import starting.growthon.exception.TargetNotFoundException;
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

    private final YearRepository yearRepository;

    private final MentorAndScheduleRepository mentorAndScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    @Autowired
    private FileRepository fileRepository;
    private final JobRepository jobRepository;

    private final MentorAndBadgeRepository mentorAndBadgeRepository;
    private final BadgeRepository badgeRepository;

    private final CompanyTypeRepository companyTypeRepository;

    public MentorService(MentorInfoRepository mentorInfoRepository, UserRepository userRepository,
                         UserUtil userUtil,
                         CompanyRepository companyRepository,
                         SubJobRepository subJobRepository,
                         FollowRepository followRepository, YearRepository yearRepository,
                         MentorAndScheduleRepository mentorAndScheduleRepository,
                         ScheduleRepository scheduleRepository, JobRepository jobRepository,
                         MentorAndBadgeRepository mentorAndBadgeRepository, BadgeRepository badgeRepository,
                         CompanyTypeRepository companyTypeRepository) {
        this.mentorInfoRepository = mentorInfoRepository;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
        this.companyRepository = companyRepository;
        this.subJobRepository = subJobRepository;
        this.followRepository = followRepository;
        this.yearRepository = yearRepository;
        this.mentorAndScheduleRepository = mentorAndScheduleRepository;
        this.scheduleRepository = scheduleRepository;
        this.jobRepository = jobRepository;
        this.mentorAndBadgeRepository = mentorAndBadgeRepository;
        this.badgeRepository = badgeRepository;
        this.companyTypeRepository = companyTypeRepository;
    }

    public MentorInfo changeRole() {
        User targetUser = userUtil.getLoggedInUser();
        targetUser.setRole("MENTOR");
        giveNewbieBadge(targetUser);
        return createMentorInfo(targetUser);
    }

    private MentorInfo createMentorInfo(User targetUser) {
        if (mentorInfoRepository.findByMentorId(targetUser.getId()) == null) {
            MentorInfo mentorInfo = new MentorInfo("", 0, 0, 0, targetUser);
            return mentorInfoRepository.save(mentorInfo);
        }
        return mentorInfoRepository.findByMentorId(targetUser.getId());
    }

    public List<MentorInfoResponseDto> getMentors() {
        ArrayList<MentorInfoResponseDto> allMentors = new ArrayList<>();
        List<User> mentors = userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("MENTOR") && mentorInfoRepository
                        .findByMentorId(user.getId()).isVerified()).toList();
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
        allMentors.addAll(mentorFindByNickname(condition));
        return new ArrayList<>(new HashSet<>(allMentors));
    }


    private List<MentorInfoResponseDto> mentorFindByCompany(String company) {
        List<Company> companies = companyRepository.findAllByNameContaining(company);

        if (companies.size() == 0 || company.isEmpty())
            return new ArrayList<>();

        ArrayList<MentorInfoResponseDto> mentorInfos = new ArrayList<>();

        for (Company com : companies) {
            List<User> companyMentors = userRepository.findAllByCompanyId(com.getId()).stream().filter(
                    user -> user.getRole().equals("MENTOR") && mentorInfoRepository
                            .findByMentorId(user.getId()).isVerified()
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
                    user -> user.getRole().equals("MENTOR") && mentorInfoRepository
                            .findByMentorId(user.getId()).isVerified()
            ).toList();
            for (User mentor : subJobMentors) {
                createMentorInfoDtoUsingMentor(mentorInfos, mentor);
            }
        }
        return mentorInfos;
    }

    private List<MentorInfoResponseDto> mentorFindByNickname(String condition) {
        List<User> mentors = userRepository.findAllByNicknameContaining(condition).stream().filter(
                user -> user.getRole().equals("MENTOR")
        ).toList();

        if (mentors.size() == 0 || condition.isEmpty())
            return new ArrayList<>();

        ArrayList<MentorInfoResponseDto> mentorInfos = new ArrayList<>();
        for (User mentor : mentors) {
            createMentorInfoDtoUsingMentor(mentorInfos, mentor);
        }

        return mentorInfos;
    }

    private void createMentorInfoDtoUsingMentor(ArrayList<MentorInfoResponseDto> allMentors, User mentor) {
        MentorInfo mentorInfo = null;
        if (mentorInfoRepository.findByMentorId(mentor.getId()) != null)
            mentorInfo = mentorInfoRepository.findByMentorId(mentor.getId());

        List<Follow> followers = followRepository.findAllByMentorId(mentor.getId());
        List<Schedule> schedules = mentorAndScheduleRepository.findAllByMentorId(mentorInfo.getId())
                .stream().map(MentorAndSchedule::getSchedule).toList();

        File img = fileRepository.findByTypeAndOwnerId("PROFILE", mentor.getId());
        String imgUrl = null;
        if (img != null) {
            imgUrl = img.getUrl();
        }
        allMentors.add(
                MentorInfoResponseDto.builder()
                        .mentor(mentor)
                        .title(mentorInfo.getTitle())
                        .introduce(mentorInfo.getIntroduce())
                        .possibles(mentorInfo.getPossibles())
                        .concept(mentorInfo.getConcept())
                        .target(mentorInfo.getTarget())
                        .prepare(mentorInfo.getPrepare())
                        .curriculum(mentorInfo.getCurriculum())
                        .time(mentorInfo.getTime())
                        .cost(mentorInfo.getCost())
                        .view(mentorInfo.getView())
                        .followers(followers.size())
                        .bank(mentorInfo.getBank())
                        .bankNumber(mentorInfo.getBankNumber())
                        .bankOwner(mentorInfo.getBankOwner())
                        .verified(mentorInfo.isVerified())
                        .profile(imgUrl)
                        .schedules(extractSchedule(schedules))
                        .badges(mentorAndBadgeRepository.findAllByMentorId(mentor.getId()).stream().map(
                                mentorAndBadge -> mentorAndBadge.getBadge().getName()
                        ).collect(Collectors.toList()))
                        .build()
        );
    }

    public MentorInfoResponseDto infoUpload(MentorInfoRequestDto dto) {
        User mentor = userUtil.getLoggedInUser();

        checkIsMentor(mentor);

        MentorInfo mentorInfo = mentorInfoRepository.findByMentorId(mentor.getId());

        mentor.setName(dto.getName());
        mentor.setNickname(dto.getNickname());

        Company company = ifComapnyNullCreate(dto.getCompany(), dto.getCompanyType());

        mentor.setCompany(company);
        mentor.setYear(yearRepository.findByName(dto.getYear()));

        setJobAndSubJob(dto, mentor);

        mentorInfo.setTitle(dto.getTitle());
        mentorInfo.setIntroduce(dto.getIntroduce());
        mentorInfo.setPossibles(dto.getPossibles());
        mentorInfo.setConcept(dto.getConcept());
        mentorInfo.setTarget(dto.getTarget());
        mentorInfo.setPrepare(dto.getPrepare());
        mentorInfo.setCurriculum(dto.getCurriculum());
        mentorInfo.setTime(dto.getTime());
        mentorInfo.setCost(dto.getCost());
        mentorInfo.setBank(dto.getBank());
        mentorInfo.setBankOwner(dto.getBankOwner());
        mentorInfo.setBankNumber(dto.getBankNumber());

        for (ScheduleDto schedule : dto.getSchedules()) {
            for (String time : schedule.getTime()) {
                if (scheduleRepository.findByDayAndTime(schedule.getDay(), time) == null) {
                    Schedule newSchedule = scheduleRepository.save(new Schedule(schedule.getDay(), time));
                    mentorAndScheduleRepository.save(new MentorAndSchedule(newSchedule, mentorInfo));
                }
            }
        }

        File img = fileRepository.findByTypeAndOwnerId("PROFILE", mentor.getId());
        String imgUrl = null;
        if (img != null) {
            imgUrl = img.getUrl();
        }

        List<Follow> followers = followRepository.findAllByMentorId(mentor.getId());
        List<Schedule> schedules = mentorAndScheduleRepository.findAllByMentorId(mentorInfo.getId())
                .stream().map(MentorAndSchedule::getSchedule).toList();

        return MentorInfoResponseDto.builder()
                .mentor(mentor)
                .title(dto.getTitle())
                .introduce(dto.getIntroduce())
                .possibles(dto.getPossibles())
                .concept(dto.getConcept())
                .target(dto.getTarget())
                .prepare(dto.getPrepare())
                .curriculum(dto.getCurriculum())
                .time(dto.getTime())
                .cost(mentorInfo.getCost())
                .profile(imgUrl)
                .schedules(extractSchedule(schedules))
                .followers(followers.size())
                .bank(dto.getBank())
                .bankOwner(dto.getBankOwner())
                .bankNumber(dto.getBankNumber())
                .verified(mentorInfo.isVerified())
                .view(mentorInfo.getView())
                .badges(mentorAndBadgeRepository.findAllByMentorId(mentor.getId()).stream().map(
                        mentorAndBadge -> mentorAndBadge.getBadge().getName()
                ).collect(Collectors.toList()))
                .build();
    }

    private Company ifComapnyNullCreate(String company, String company_type) {
        Company exist = companyRepository.findByName(company);
        CompanyType existType = companyTypeRepository.findByName(company_type);
        if (exist == null) {
            Company newCompany = new Company();
            newCompany.setName(company);
            newCompany.setCompanyType(existType);
            return companyRepository.save(newCompany);
        }
        return exist;
    }

    private List<ScheduleDto> extractSchedule(List<Schedule> schedules) {
        List<ScheduleDto> result = new ArrayList<>();

        for (Schedule schedule : schedules) {
            boolean found = false;
            for (ScheduleDto r : result) {
                if (r.getDay().equals(schedule.getDay())) {
                    r.getTime().add(schedule.getTime());
                    found = true;
                    break;
                }
            }
            if (!found) {
                List<String> newTime = new ArrayList<>();
                newTime.add(schedule.getTime());
                result.add(new ScheduleDto(schedule.getDay(), newTime));
            }
        }

        return result;
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

    private void giveNewbieBadge(User mentor) {
        List<MentorAndBadge> mentorBadges = mentorAndBadgeRepository.findAllByMentorId(mentor.getId());
        if (mentorBadges.isEmpty())
            mentorAndBadgeRepository.save(new MentorAndBadge(mentor, badgeRepository.findByName("신규")));
    }

    public MentorInfo getMentor(Long uuid) {
        User mentor = userRepository.findByUuid(uuid).filter(user -> user.getRole().equals("MENTOR")).get();
        if (mentor == null)
            throw new TargetNotFoundException("멘토가 없습니다.");
        MentorInfo result = mentorInfoRepository.findByMentorId(mentor.getId());
        if (result == null)
            throw new TargetNotFoundException("멘토가 없습니다.");
    }
}
