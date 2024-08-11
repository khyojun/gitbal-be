package gitbal.backend.domain.region.application;

import gitbal.backend.domain.region.Region;
import gitbal.backend.domain.region.application.repository.RegionRepository;
import gitbal.backend.domain.user.User;
import gitbal.backend.global.exception.NotFoundRegionException;
import gitbal.backend.global.util.JoinUpdater;
import gitbal.backend.global.util.ScheduleUpdater;
import gitbal.backend.domain.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionService{

  private final RegionRepository regionRepository;
  private final UserRepository userRepository;
  private final ScheduleUpdater<Region> regionScheduleUpdater;


  public Region findByRegionName(String regionName) {
    return regionRepository.findByRegionName(regionName)
        .orElseThrow(NotFoundRegionException::new);
  }

  public void joinNewUserScore(User findUser) {
    JoinUpdater regionJoinUpdater = new RegionJoinUpdater();
    regionJoinUpdater.process(findUser);
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
    regionScheduleUpdater.update(region, username, oldScore, newScore);
  }


  public int findRegionRanking(String regionName) {
    List<Region> regionList = regionRepository.findAll(
        Sort.sort(Region.class).by(Region::getScore).descending());
    for (int i = 0; i < regionList.size(); i++) {
        if(regionList.get(i).getRegionName().equals(regionName))
            return i+1;
    }
    throw new NotFoundRegionException();
  }
}
