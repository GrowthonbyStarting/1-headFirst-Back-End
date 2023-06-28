package starting.growthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.response.FollowDto;
import starting.growthon.entity.Follow;
import starting.growthon.entity.MentorAndBadge;
import starting.growthon.entity.User;
import starting.growthon.exception.TargetNotFoundException;
import starting.growthon.repository.*;
import starting.growthon.util.UserUtil;

import java.util.stream.Collectors;

@Service
@Transactional
public class FollowService {

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private MentorInfoRepository mentorInfoRepository;
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MentorAndBadgeRepository mentorAndBadgeRepository;
    @Autowired
    private BadgeRepository badgeRepository;
    @Autowired
    private UserRepository userRepository;

    public FollowDto addFollow(Long mentorId) {
        User mentee = getLoggedInUser();
        User mentor = getMentor(mentorId);
        // 멘토는 자기 자신을 팔로우 할 수 없고, 다른 멘토를 팔로우 할 수도 없다.
        if (mentee.getUuid().equals(mentor.getUuid()))
            throw new IllegalStateException("자기 자신은 팔로우 할 수 없습니다.");
        if (mentee.getRole().equals("MENTOR"))
            throw new IllegalStateException("멘토는 멘토를 팔로우 할 수 없습니다.");
        if (followRepository.findByMentorIdAndMenteeId(mentor.getId(), mentee.getId()) == null) {
            followRepository.save(new Follow(mentor, mentee));
            checkGeneralBadge(mentorId);
            checkGoodBadge(mentorId);
            checkVIPBadge(mentorId);
            checkVVIPBadge(mentorId);
            checkMasterBadge(mentorId);
        }
        return new FollowDto(mentor.getUuid(), mentee.getUuid());
    }

    public void cancelFollow(Long mentorId) {
        User mentee = getLoggedInUser();
        User mentor = getMentor(mentorId);
        if (followRepository.findByMentorIdAndMenteeId(mentor.getId(), mentee.getId()) == null)
            throw new IllegalStateException("팔로우 관계가 아닙니다.");
        followRepository.deleteByMentorIdAndMenteeId(mentor.getId(), mentee.getId());
    }

    private User getLoggedInUser() {
        return userUtil.getLoggedInUser();
    }

    private User getMentor(Long mentorId) {
        if (mentorInfoRepository.findByMentorId(mentorId) != null)
            return mentorInfoRepository.findByMentorId(mentorId).getMentor();
        throw new TargetNotFoundException("해당 멘토가 없습니다.");
    }

    private void checkGeneralBadge(Long mentorId) {
        if (followRepository.findAllByMentorId(mentorId).size() >= 10 &&
                mentorAndBadgeRepository.findAllByMentorId(mentorId).stream().noneMatch(
                        mentorAndBadge -> mentorAndBadge.getBadge().getName().equals("일반"))) {
            mentorAndBadgeRepository.save(new MentorAndBadge(userRepository.findById(mentorId).get(),
                    badgeRepository.findByName("일반")));
        }
    }
    private void checkGoodBadge(Long mentorId) {
        if (followRepository.findAllByMentorId(mentorId).size() >= 30 &&
                mentorAndBadgeRepository.findAllByMentorId(mentorId).stream().noneMatch(
                        mentorAndBadge -> mentorAndBadge.getBadge().getName().equals("우수"))) {
            mentorAndBadgeRepository.save(new MentorAndBadge(userRepository.findById(mentorId).get(),
                    badgeRepository.findByName("우수")));
        }
    }
    private void checkVIPBadge(Long mentorId) {
        if (followRepository.findAllByMentorId(mentorId).size() >= 50 &&
                mentorAndBadgeRepository.findAllByMentorId(mentorId).stream().noneMatch(
                        mentorAndBadge -> mentorAndBadge.getBadge().getName().equals("VIP"))) {
            mentorAndBadgeRepository.save(new MentorAndBadge(userRepository.findById(mentorId).get(),
                    badgeRepository.findByName("VIP")));
        }
    }
    private void checkVVIPBadge(Long mentorId) {
        if (followRepository.findAllByMentorId(mentorId).size() >= 80 &&
                mentorAndBadgeRepository.findAllByMentorId(mentorId).stream().noneMatch(
                        mentorAndBadge -> mentorAndBadge.getBadge().getName().equals("VVIP"))) {
            mentorAndBadgeRepository.save(new MentorAndBadge(userRepository.findById(mentorId).get(),
                    badgeRepository.findByName("VVIP")));
        }
    }
    private void checkMasterBadge(Long mentorId) {
        if (followRepository.findAllByMentorId(mentorId).size() >= 100 &&
                mentorAndBadgeRepository.findAllByMentorId(mentorId).stream().noneMatch(
                        mentorAndBadge -> mentorAndBadge.getBadge().getName().equals("마스터"))) {
            mentorAndBadgeRepository.save(new MentorAndBadge(userRepository.findById(mentorId).get(),
                    badgeRepository.findByName("마스터")));
        }
    }
}
