package com.qo_op.api.service;

import com.qo_op.api.comm.AES128;
import com.qo_op.api.comm.SHA256;
import com.qo_op.api.dto.AccountDto;
import com.qo_op.api.exception.AuthFailException;
import com.qo_op.api.exception.LoginFailException;
import com.qo_op.api.model.Auth;
import com.qo_op.api.model.Member;
import com.qo_op.api.repository.AuthRepository;
import com.qo_op.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private static final String ACCOUNT_USER_KEY = "ACCOUNT_USER_KEY";
    private static final String AUTH_KEY = "AUTH_KEY";
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    private final AES128 aes128;

    public Member signUp(final AccountDto.SignUpReq dto) throws Exception {

        Member member = memberRepository.save(dto.toEntity());
        return member;
    }

    @Transactional
    public String signIn(AccountDto.LoginReq dto) throws Exception {
        Member member = memberRepository.findByNickNameOrEmailOrPhone(dto.getLoginName(), dto.getLoginName(), dto.getLoginName());
        if (member == null) throw new LoginFailException("회원 정보가 존재하지 않습니다.");
        if (!member.isPasswordValidation(dto.getPassword()))
            throw new LoginFailException("회원 정보가 일치하지 않습니다. ");
        String token = aes128.encrypt(ACCOUNT_USER_KEY, member.getEmail() + System.currentTimeMillis());
        member.login(token);

        log.info(token);
        log.info(member.toString());
        return token;
    }

    public Auth authIssue(AccountDto.AuthIssueReq dto) {
        Auth auth = authRepository.save(dto.toEntity());
        return auth;
    }

    public String authCheck(AccountDto.AuthReq dto) throws Exception {
        Optional<Auth> authOptional = authRepository.findById(dto.getId());
        if(!authOptional.isPresent()){
            throw new AuthFailException("인증 정보가 일치하지 않습니다. ");
        }
        Auth auth = authOptional.get();
        if(auth.isValid(dto)){
            throw new AuthFailException("인증 정보가 일치하지 않습니다. ");
        }
        auth.updateToken(aes128.encrypt(ACCOUNT_USER_KEY, dto.getPhone() + System.currentTimeMillis()));
        authRepository.save(auth);
        return auth.getToken();
    }


    public boolean authVerification(String authToken, Auth.AuthType authType) {
        Optional<Auth> authOptional = authRepository.findByTokenAndAuthType(authToken, authType);
        return authOptional.isPresent();
    }

    public void changePassword(AccountDto.ChangePasswordReq dto) throws Exception {
        Optional<Auth> authOptional = authRepository.findByTokenAndAuthType(dto.getAuthToken(), Auth.AuthType.FIND_PASSWORD);
        if( authOptional.isPresent()){
            throw new AuthFailException("인증 정보가 일치하지 않습니다. ");
        }
        Auth auth = authOptional.get();
        Member member = memberRepository.findByPhone(auth.getPhone());
        if(member == null){
            throw new Exception("회원정보가 존재하지 않습니다.");
        }
        member.updatePassword(new SHA256().encrypt(dto.getPassword()));
        memberRepository.save(member);
    }
}

