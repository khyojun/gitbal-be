package gitbal.backend.api.regionPage.service;


import gitbal.backend.api.regionPage.dto.RegionListPageResponseDto;
import gitbal.backend.domain.region.Region;
import gitbal.backend.domain.user.User;
import gitbal.backend.api.regionPage.dto.FirstRankRegionDto;
import gitbal.backend.api.regionPage.dto.MyRegionInfoResponseDto;
import gitbal.backend.api.regionPage.dto.RegionListDto;
import gitbal.backend.global.exception.NotFoundRegionException;
import gitbal.backend.global.exception.NotFoundUserException;
import gitbal.backend.global.exception.NotLoginedException;
import gitbal.backend.domain.region.RegionRepository;
import gitbal.backend.domain.user.UserRepository;
import gitbal.backend.global.security.CustomUserDetails;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO : 이후에 regionChangeScore 반영해야함!


@Service
@RequiredArgsConstructor
public class RegionRankService {

    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    private final int page = 1;

    public ResponseEntity<RegionListPageResponseDto<RegionListDto>> getRegionList() {
        Sort sort = Sort.by("score").descending();
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<Region> regionPage = regionRepository.findAll(pageable);

        List<RegionListDto> regionDtoList = regionPage.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

        // 3. PageResponseDto 생성
        RegionListPageResponseDto<RegionListDto> RegionList = RegionListPageResponseDto.<RegionListDto>withALl()
            .regionList(regionDtoList)
            .build();

        return ResponseEntity.ok(RegionList);
    }

    public ResponseEntity<FirstRankRegionDto> getFirstRankRegionnfo() {
        Region firstRegion = regionRepository.firstRankedRegion(); // TODO: 우선 가장 높은 점수의 학교를 가져오는 쿼리로 가져옴. (나중엔 미리 점수별로 정렬해둘 것이므로 수정)
        FirstRankRegionDto firstRankInfo = FirstRankRegionDto.builder()
            .regionName(firstRegion.getRegionName())
            .regionScore(firstRegion.getScore())
            .regionChangeScore(0L) // TODO : 이후에 점수 바뀌는 로직 적용해야함.
            .mvpName(firstRegion.getTopContributor())
            .build();

        return ResponseEntity.ok(firstRankInfo);
    }

    private RegionListDto convertToDto(Region region) {
        return new RegionListDto(
            region.getRegionName(),
            region.getScore(),
            0L, // TODO : 이후에 점수 바뀌는 로직 적용해야함.
            region.getTopContributor()
        );

    }

    @Transactional(readOnly = true)
    public MyRegionInfoResponseDto getMyRegionInfo(Authentication authentication) {
        if (authentication == null) {
            throw new NotLoginedException();
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String username = principal.getNickname();
        User user = userRepository.findByNickname(username).orElseThrow(
            NotFoundUserException::new
        );
        Region region = user.getRegion();
        if(Objects.isNull(region)) return MyRegionInfoResponseDto.of(0, null);
        return MyRegionInfoResponseDto.of(findRegionRank(region.getRegionName()), region);
    }

    //TODO: 이후에 school, region 관련하여서 더 생각해보기
    private int findRegionRank(String regionName){
        List<Region> regions = regionRepository.findAll(Sort.by("score").descending());
        for (int i = 0; i < regions.size(); i++) {
            Region region = regions.get(i);
            if(region.getRegionName().equals(regionName))  return i+1;
        }
        throw new NotFoundRegionException();
    }
}
