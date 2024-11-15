package gitbal.backend.api.mainPage.service;

import gitbal.backend.api.mainPage.dto.MainPageUserDto;
import gitbal.backend.api.mainPage.dto.MainPageUserResponseDto;
import gitbal.backend.api.schoolPage.dto.SchoolListPageResponseDto;
import gitbal.backend.domain.user.User;
import gitbal.backend.domain.user.UserRepository;
import gitbal.backend.global.constant.Grade;
import gitbal.backend.global.dto.PageInfoDto;
import gitbal.backend.global.exception.PageOutOfRangeException;
import gitbal.backend.global.exception.WrongPageNumberException;
import gitbal.backend.global.util.PageCalculator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainPageService {

    private static final int PAGE_SIZE = 14;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public MainPageUserResponseDto getUsers(int page, String searchedname) {
        if (!Objects.isNull(searchedname))   return getSearchedUserList(searchedname, page);
        try {
            Page<User> findUserPages = userRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("score").descending()));
            validatePage(page, findUserPages.getTotalElements());
            log.info(String.valueOf(findUserPages.getTotalElements()));

            List<MainPageUserDto> users = convertToMainPageUserDto(findUserPages);

            PageInfoDto pageInfoDto = PageCalculator.calculatePageInfo(findUserPages);

            return MainPageUserResponseDto.of(users, pageInfoDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongPageNumberException(page);
        }
    }

    private MainPageUserResponseDto getSearchedUserList(String searchedname, int page) {
        try {
            Page<User> searchUsersIgnoreCase = userRepository.findByNicknameContainingIgnoreCase(
                searchedname,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("score").descending()));
            if (isSearchedUserNone(searchUsersIgnoreCase)) {
                if(page >1){
                    throw new PageOutOfRangeException();
                }
                return MainPageUserResponseDto.of(List.of(), new PageInfoDto(0, 0, 0, 0));
            }
            if (searchUsersIgnoreCase.getTotalPages() < page) {
                throw new PageOutOfRangeException();
            }


            List<MainPageUserDto> searchUserList = convertToMainPageUserDto(
                searchUsersIgnoreCase);
            PageInfoDto pageInfoDto = PageCalculator.calculatePageInfo(searchUsersIgnoreCase);

            return MainPageUserResponseDto.of(searchUserList, pageInfoDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WrongPageNumberException(page);
        }
    }

    private List<MainPageUserDto> convertToMainPageUserDto(Page<User> users) {
        return users.stream().map(
            (u) -> new MainPageUserDto(u.getNickname(), u.getProfile_img(), u.getScore(),
                u.getUserRank())
        ).toList();
    }

    private boolean isSearchedUserNone(Page<User> searchUsersIgnoreCase) {
        return searchUsersIgnoreCase.getTotalElements() == 0;
    }


    private void validatePage(int pageNumber, long totalNumber) {
        if (checkRemainPage(pageNumber, totalNumber)) {
            return;
        }

        if (rangeCheck(pageNumber, totalNumber)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean rangeCheck(int pageNumber, long totalNumber) {
        return (long) pageNumber * PAGE_SIZE > totalNumber || pageNumber < 0;
    }

    private boolean checkRemainPage(int pageNumber, long totalNumber) {
        return (long) pageNumber * PAGE_SIZE - totalNumber < PAGE_SIZE;
    }

    @Transactional(readOnly = true)
    public MainPageUserResponseDto getGradeUsers(int page, Grade grade) {
        try{
            Page<User> findGradeUsers = userRepository.findUserByGrade(grade,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("score").descending())
            );
            validatePage(page, findGradeUsers.getTotalElements());
            log.info(String.valueOf(findGradeUsers.getTotalElements()));

            List<MainPageUserDto> users = convertToMainPageUserDto(findGradeUsers);

            PageInfoDto pageInfoDto = PageCalculator.calculatePageInfo(findGradeUsers);
            return MainPageUserResponseDto.of(users, pageInfoDto);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            throw new WrongPageNumberException(page);
        }


    }
}
