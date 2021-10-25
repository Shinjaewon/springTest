package com.qo_op.api.controller;

import com.qo_op.api.comm.UserTokenStore;
import com.qo_op.api.dto.AccountDto;
import com.qo_op.api.exception.AuthFailException;
import com.qo_op.api.model.Auth;
import com.qo_op.api.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Account")
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;


    @ApiOperation(value = "인증", response = AccountDto.LoginRes.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccountDto.LoginRes.class)
    })
    @PostMapping(value = "/auth/issue")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.AuthIssueRes authIssue(@Valid @RequestBody final AccountDto.AuthIssueReq dto) throws Exception {
        return new AccountDto.AuthIssueRes(accountService.authIssue(dto));
    }

    @ApiOperation(value = "인증 체크", response = AccountDto.LoginRes.class)
    @PostMapping(value = "/auth/check")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.AuthRes authCheck(@Valid @RequestBody final AccountDto.AuthReq dto) throws Exception {
        return new AccountDto.AuthRes(accountService.authCheck(dto));
    }

    @ApiOperation(value = "로그인", response = AccountDto.LoginRes.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccountDto.LoginRes.class)
    })
    @PostMapping(value = "/signIn")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.LoginRes signIn(@Valid @RequestBody final AccountDto.LoginReq dto) throws Exception {
        return new AccountDto.LoginRes(accountService.signIn(dto));
    }

    @ApiOperation(value = "회원 가입", response = AccountDto.MemberRes.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccountDto.MemberRes.class)
    })
    @PostMapping(value = "/signUp")
    @ResponseStatus(value = HttpStatus.CREATED)
    public AccountDto.MemberRes signUp(@Valid @RequestBody final AccountDto.SignUpReq dto) throws Exception {
        if(accountService.authVerification(dto.getAuthToken(), Auth.AuthType.JOIN)){
            throw new AuthFailException("인증 정보가 일치하지 않습니다. ");
        }
        return new AccountDto.MemberRes(accountService.signUp(dto));
    }

    @ApiOperation(value = "내 정보", response = AccountDto.MemberRes.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = AccountDto.MemberRes.class),
    })
    @GetMapping(value = "/myInfo")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountDto.MemberRes myInfo(@RequestHeader("Authorization") String token) {

        return new AccountDto.MemberRes(UserTokenStore.get());
    }

    @ApiOperation(value = "패스워드 변경")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
    })
    @PostMapping(value = "/changePassword")
    @ResponseStatus(value = HttpStatus.OK)
    public void changePassword(@Valid @RequestBody final AccountDto.ChangePasswordReq dto) throws Exception {

        accountService.changePassword(dto);
    }


}
