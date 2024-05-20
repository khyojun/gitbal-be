package gitbal.backend.domain.auth.controller;

import gitbal.backend.domain.auth.dto.JoinRequestDto;
import gitbal.backend.domain.auth.service.LoginService;
import gitbal.backend.global.dto.UserInfoDto;
import gitbal.backend.domain.auth.service.UserInfoService;
import gitbal.backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final UserInfoService userInfoService;


    @PostMapping("/join")
    @Operation(summary = "회원가입 (구현 완료)", description = "회원가입을 위한 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다."),
    })
    public ResponseEntity<String> login(Authentication authentication,
        @RequestBody JoinRequestDto joinRequestDto, HttpServletResponse response) {

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        loginService.join(joinRequestDto, principal);

        response.setHeader("accessToken", principal.getAccessToken());
        return ResponseEntity.ok("회원 가입 성공!");
    }

    @GetMapping("/logincheck")
    public String joinTest(Authentication authentication, HttpServletResponse response) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        System.out.println(principal.getNickname());
        response.setHeader("AccessToken", principal.getAccessToken());
        return "hello";
    }

    @GetMapping("/userInfo/{username}")
    @Operation(summary = "유저 정보 조회 (구현 완료)", description = "유저 정보 조회를 위한 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 정보를 가져오는데 성공했습니다."),
        @ApiResponse(responseCode = "400", description = "유저 정보를 가져오는데 실패했습니다.")
    })
    public ResponseEntity<UserInfoDto> userInfo(@PathVariable String username) {
        return userInfoService.getUserInfoByUserName(username);
    }


    @GetMapping("/logout")
    @Operation(summary = "로그아웃 (미구현)", description = "로그아웃을 위한 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃에 성공했습니다."),
        @ApiResponse(responseCode = "5xx", description = "로그아웃에 실패했습니다.")
    })
    public void logout() {

    }


    @DeleteMapping("/withdraw")
    @Operation(summary = "회원탈퇴 (구현 완료)", description = "회원탈퇴를 위한 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원탈퇴에 성공했습니다."),
        @ApiResponse(responseCode = "5xx", description = "회원탈퇴에 실패했습니다.")
    })
    public ResponseEntity<String> withdrawService(Authentication authentication) {
        return ResponseEntity.ok(loginService.withDrawUser(authentication));
    }
}
