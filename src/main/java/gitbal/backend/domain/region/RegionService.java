package gitbal.backend.domain.region;

import gitbal.backend.domain.user.User;
import gitbal.backend.global.exception.NotFoundRegionException;
import gitbal.backend.global.util.SurroundingRankStatus;
import gitbal.backend.domain.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionService {

  private final RegionRepository regionRepository;
  private final UserRepository userRepository;
  private final int REGION_AROUND_RANGE = 3;


  public Region findByRegionName(String regionName) {
    return regionRepository.findByRegionName(regionName)
        .orElseThrow(NotFoundRegionException::new);
  }

  public void joinNewUserScore(User findUser) {
    Long score = findUser.getScore();
    Region region = findUser.getRegion();
    region.addScore(score);
  }

  public RegionRaceStatus findRegionScoreRaced(Long score) {
    int forwardCount = regionRepository.regionScoreRacedForward(score);
    int backwardCount = regionRepository.regionScoreRacedBackward(score);
    log.info("forwardCount = {} backwardCount = {}", forwardCount, backwardCount);

    SurroundingRankStatus surroundingRankStatus = SurroundingRankStatus.calculateSchoolRegionForwardBackward(
        forwardCount, backwardCount, REGION_AROUND_RANGE);

    log.info("after forwardCount = {} backwardCount = {}", forwardCount, backwardCount);
    return RegionRaceStatus.of(
        regionRepository.regionScoreRaced(score, surroundingRankStatus.getForwardCount(),
            surroundingRankStatus.getBackwardCount()));
  }

  public void insertTopContributorInfo() {
    List<User> users = userRepository.findAll();
    for (Region region : regionRepository.findAll()) {
      for (User user : users) {
        if (user.getRegion() != null && user.getRegion().getRegionId() == region.getRegionId()) {
          region.updateContributerInfo(user.getNickname(), user.getScore());
          regionRepository.save(region);
        }
      }
    }
  }




  public void updateByUserScore(Region region, String username, Long oldScore, Long newScore) {
    region.updateScore(oldScore,newScore);
    region.updateContributerInfo(username, newScore);
  }
}
