package gitbal.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regionRank")
@Tag(name= "지역 랭킹 API(미구현)", description = "지역 랭킹 페이지를 위한 api입니다.")
public class RegionRankController {

    @GetMapping("/myRegion")
    @Operation(summary = "내 지역 정보", description = "내 지역 정보를 나타내는 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "내 지역 정보 요청을 성공했습니다."),
        @ApiResponse(responseCode = "5xx", description = "내 지역 정보 요청을 실패했습니다.")
    })
    public void myRegion(){ // TODO : 로그인한 유저로 봐야함.

    }


    @GetMapping("/regionList")
    @Operation(summary = "지역 리스트", description = "지역 리스트를 나타내는 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "지역 리스트 요청을 성공했습니다."),
        @ApiResponse(responseCode = "5xx", description = "지역 리스트 요청을 실패했습니다.")
    })
    public void regionList(){

    }



//    /api/v1/regionRank/myRegion	get
///api/v1/regionRank/regionList	get


}
