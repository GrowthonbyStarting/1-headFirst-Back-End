package starting.growthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starting.growthon.dto.response.FollowDto;
import starting.growthon.entity.Follow;
import starting.growthon.entity.User;
import starting.growthon.exception.TargetNotFoundException;
import starting.growthon.repository.FollowRepository;
import starting.growthon.repository.MentorInfoRepository;
import starting.growthon.util.UserUtil;

@Service
@Transactional
public class FollowService {

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private MentorInfoRepository mentorInfoRepository;
    @Autowired
    private FollowRepository followRepository;

    public FollowDto addFollow(Long mentorId) {
        User mentee = getLoggedInUser();
        User mentor = getMentor(mentorId);
        // 멘토는 자기 자신을 팔로우 할 수 없고, 다른 멘토를 팔로우 할 수도 없다.
        if (mentee.getUuid().equals(mentor.getUuid()))
            throw new IllegalStateException("자기 자신은 팔로우 할 수 없습니다.");
        if (mentee.getRole().equals("MENTOR"))
            throw new IllegalStateException("멘토는 멘토를 팔로우 할 수 없습니다.");
        if (followRepository.findByMentorIdAndMenteeId(mentor.getId(), mentee.getId()) == null)
            followRepository.save(new Follow(mentor, mentee));
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
}
