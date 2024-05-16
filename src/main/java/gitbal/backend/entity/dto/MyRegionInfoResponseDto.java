package gitbal.backend.entity.dto;

import gitbal.backend.entity.Region;
import lombok.Getter;

@Getter
public class MyRegionInfoResponseDto {


    private int myRegionRank;
    private final String myRegionName;
    private final long totalRegionScore;
    private final String mvpName;
    private final long mvpScore;


    public MyRegionInfoResponseDto(int myRegionRank, String myRegionName, long totalRegionScore, String mvpName,
        long mvpScore) {
        this.myRegionRank = myRegionRank;
        this.myRegionName = myRegionName;
        this.totalRegionScore = totalRegionScore;
        this.mvpName = mvpName;
        this.mvpScore = mvpScore;
    }

    public static MyRegionInfoResponseDto of(int regionRank, Region region) {
        return new MyRegionInfoResponseDto(regionRank, region.getRegionName(), region.getScore(),
            region.getTopContributor(), region.getContributorScore());
    }

}