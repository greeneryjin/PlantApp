package com.example.demo.account.controller;

import com.example.demo.account.dto.request.saveDto.AccountUpdateDto;
import com.example.demo.account.dto.request.saveDto.AddRegisterDto;
import com.example.demo.account.dto.response.viewDto.AccountProfileDto;
import com.example.demo.account.entity.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.headerconfig.Header;
import com.example.demo.headerconfig.ResponseResult;
import com.example.demo.security.entity.RefreshToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Api(tags = "유저")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    /**
     * sns 추가입력
     * 닉네임 중복 처리
     * 추가 정보 입력
     * 회원 사진 조회
     * 회원 수정
     * 회원 조회
     * 회원 리프레시 토큰 재발급
     * 회원 로그아웃
     * 회원 탈퇴
     * */
    private final AccountService accountService;

    @ApiOperation(value = "추가정보")
    @PutMapping("/register")
    public Header<Long> registerUser(@ApiParam(value = "주소회원 닉네임, 약관 동의", required = true)
                                     @RequestBody AddRegisterDto addRegisterDto) throws IOException, java.text.ParseException, org.json.simple.parser.ParseException {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Long accountId = accountService.saveAccount(account, addRegisterDto);
        return Header.description(accountId, "회원등록이 완료되었습니다.");
    }

    //닉네임 중복 확인 조회
    @ApiOperation(value = "닉네임 중복 확인")
    @GetMapping("/login/nickName/duplicate")
    public Header<String> duplicateNickName(@ApiParam(value = "닉네임", required = true, example = "in_.77")
                                            @RequestParam String nickName) {
        boolean duplicateNickName = accountService.duplicateNickName(nickName);
        if (duplicateNickName) {
            return Header.OK("다른 유저가 사용하는 닉네임입니다. 다른 닉네임으로 만들어주세요");
        } else {
            return Header.OK("사용가능한 닉네임입니다.");
        }
    }

    @ApiOperation(value = "이메일 중복 확인", notes = "이메일 형식 준수")
    @GetMapping("/login/email/duplicate")
    public Header<String> duplicateEmail(@ApiParam(value = "이메일", required = true, example = "wlsi@dddd.com")
                                         @RequestParam @Email @NotNull String email) {
        boolean duplicateEmail = accountService.duplicateEmail(email);
        if (duplicateEmail) {
            return Header.OK("이미 사용 중인 이메일입니다.");
        } else {
            return Header.OK("사용가능한 이메일입니다.");
        }
    }

    //회원 사진 조회
    @ApiOperation(value = "유저 사진")
    @GetMapping("/profile/picture/view")
    public Header<String> pictureView() {

        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        String pictureView = accountService.pictureView(account);
        return Header.OK(pictureView);
    }

    //회원 정보 수정
    @PutMapping(value = "/profile/update", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "유저 정보 수정, 사진 저장과 같은 application/json 형식")
    public Header registerSelfInfo(@RequestPart(value = "accountUpdateDto", required = false) AccountUpdateDto accountUpdateDto,
                                   @RequestPart(value="file", required = false) MultipartFile file) throws IOException {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        Account updateAccount = accountService.updateAccount(account, accountUpdateDto, file);
        return Header.description(updateAccount.getId(), "회원 수정이 완료되었습니다.");
    }

    @ApiOperation(value = "유저 조회")
    @GetMapping("/profile/view")
    public Header<AccountProfileDto> MyProfileView() {

        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        AccountProfileDto profileDto = accountService.profileView(account);
        return Header.OK(profileDto);
    }

    //액세스 토큰 재발급
    @ApiOperation(value = "리프레시 토큰", notes = "액세스 토큰 재발급")
    @GetMapping("/login/token/renew")
    public void tokenRenew() {}

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public void logoutUser(HttpServletResponse response) throws IOException {
        ResponseResult result = new ResponseResult();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //유저 찾기
            Account principal = (Account) authentication.getPrincipal();
            Long accountId = principal.getId();
            result.logoutResponse(response);
        }
    }

    @ApiOperation(value = "회원탈퇴")
    @DeleteMapping("/delete")
    public void deleteUser() {
        //사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        accountService.deleteUser(account);
    }
}
