package gitbal.backend.api.guestBookPage.controller;

import gitbal.backend.api.guestBookPage.dto.GuestBookResponseDto;
import gitbal.backend.api.guestBookPage.dto.GuestBookWriteRequestDto;
import gitbal.backend.api.guestBookPage.facade.GuestBookPageFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/guestbook")
@RequiredArgsConstructor
@Tag(name = "게시판 페이지를 위한 API")
public class GuestBookPageController {

    private final GuestBookPageFacade guestBookPageFacade;


    @GetMapping
    @Operation(summary = "현재 등록되어져있는 방명록들 최신 순으로 30개 표시 ", description = "방명록 목록을 보여주는 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "방명록 목록 전달 성공"),
        @ApiResponse(responseCode = "500", description = "방명록 목록을 서버 측에서 오류가 발생하여 전달하지 못했습니다.")
    })
    public ResponseEntity<List<GuestBookResponseDto>> getGuestBooks() {
        return ResponseEntity.ok(guestBookPageFacade.getDashBoardDtos());
    }


    @PostMapping("/write")
    @Operation(summary = "유저가 방명록을 작성할 때 (헤더에 토큰 필요 Authorization: Bearer {토큰 값 넣기})", description = "방명록을 작성하는 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "방명록 목록 전달 성공"),
        @ApiResponse(responseCode = "401", description = "인증된 유저가 없는 상태로 요청하셨습니다. github 로그인을 진행해주세요."),
        @ApiResponse(responseCode = "500", description = "방명록 목록을 서버 측에서 오류가 발생하여 전달하지 못했습니다.")
    })
    public ResponseEntity<String> writeGuestBook(Authentication authentication ,@RequestBody GuestBookWriteRequestDto guestBookWriteRequestDto) {
        guestBookPageFacade.saveDashBoard(authentication, guestBookWriteRequestDto.content());
        return ResponseEntity.ok("방명록 등록에 성공하였습니다.");
    }



}
