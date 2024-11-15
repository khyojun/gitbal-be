package gitbal.backend.api.badge.controller;



import gitbal.backend.api.badge.dto.BadgeResponseDTO;
import gitbal.backend.api.badge.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/badge")
@RestController
@RequiredArgsConstructor
public class BadgeController {

  private final BadgeService badgeService;

  @GetMapping("/get")
  @Operation(summary = "뱃지 생성을 위한 유저 정보 조회(새로 업데이트 된 api)", description = "뱃지 생성을 위한 유저 정보 조회 api입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "유저 정보를 가져오는데 성공했습니다."),
      @ApiResponse(responseCode = "400", description = "유저 정보를 가져오는데 실패했습니다.")
  })
  public ResponseEntity<BadgeResponseDTO> getBadge(@RequestParam String username) {
    return ResponseEntity.ok(badgeService.getBadgeResponse(username));
  }
}
