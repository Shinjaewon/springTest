package com.qo_op.api.dto;

import com.qo_op.api.comm.SHA256;
import com.qo_op.api.model.Auth;
import com.qo_op.api.model.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.security.NoSuchAlgorithmException;


public class AccountDto {

    @ApiModel(value = "회원 가입")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {

        @ApiModelProperty(value = "인증 토큰", required = true)
        private String authToken;

        @ApiModelProperty(value = "이메일", required = true)
        @Email
        private String email;

        @ApiModelProperty(value = "닉네임", required = true)
        @Size(min = 4, max = 100, message = "닉네임 4-100")
        private String nickName;

        @ApiModelProperty(value = "비밀번호", required = true)
        @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}", message = "패스워드는 대문자, 소문자를 포함하며 8-20자리로 구성되어야 합니다.")
        private String password;

        @ApiModelProperty(value = "이름", required = true)
        @NotEmpty
        private String userName;

        @ApiModelProperty(value = "핸드폰 번호", required = true)
        @NotEmpty
        private String phone;


        @Builder
        public SignUpReq(String email, String nickName, String password, String userName, String phone) {
            this.email = email;
            this.nickName = nickName;
            this.userName = userName;
            this.password = password;
            this.phone = phone;
        }

        public Member toEntity() throws NoSuchAlgorithmException {
            return Member.builder()
                    .email(this.email)
                    .nickName(this.nickName)
                    .userName(this.userName)
                    .password(new SHA256().encrypt(this.password))
                    .phone(this.phone)
                    .build();
        }

    }

    @ApiModel(value = "회원 정보")
    @Getter
    public static class MemberRes {
        @ApiModelProperty(value = "이메일")
        private final String email;

        @ApiModelProperty(value = "닉네임")
        private final String nickName;

        @ApiModelProperty(value = "이름")
        private final String userName;

        @ApiModelProperty(value = "핸드폰 번호")
        private final String phone;

        public MemberRes(Member member) {
            this.email = member.getEmail();
            this.nickName = member.getNickName();
            this.userName = member.getUserName();
            this.phone = member.getPhone();
        }
    }

    @ApiModel(value = "로그인 결과 정보")
    @Getter
    public static class LoginRes {
        private final String userToken;

        public LoginRes(String userToken) {
            this.userToken = userToken;
        }
    }

    @ApiModel(value = "로그인 요청 정보")
    @Getter
    public static class LoginReq {

        @ApiModelProperty(value = "로그인 아이디")
        @NotEmpty
        private String loginName;

        @ApiModelProperty(value = "비밀번호")
        @NotEmpty
        private String password;


    }

    @ApiModel(value = "인증 요청 결과 정보")
    @Getter
    public static class  AuthIssueRes {
        @ApiModelProperty(value = "인증 식별 아이디")
        private final Long id;
        @ApiModelProperty(value = "인증 번호")
        private final String authNo;

        public AuthIssueRes(Auth auth) {
            this.authNo = auth.getAuthNo();
            this.id = auth.getId();
        }
    }

    @ApiModel(value = "인증 요청 정보")
    @Getter
    public static class AuthIssueReq {
        @ApiModelProperty(value = "핸드폰 번호")
        @NotEmpty
        private String phone;

        @ApiModelProperty(value = "인증타입 (JOIN,FIND_PASSWORD)")
        @Enumerated(EnumType.STRING)
        private Auth.AuthType authType;

        public Auth toEntity() {
            return Auth.builder().authType(authType).phone(phone).build();
        }
    }

    @ApiModel(value = "인증 완료 결과 정보")
    @Getter
    public static class  AuthRes {
        private final String authToken;

        public AuthRes(String authToken) {
            this.authToken = authToken;
        }
    }


    @ApiModel(value = "인증 완료 요청 정보")
    @Getter
    public static class AuthReq {
        @ApiModelProperty(value = "인증 키")
        @NotNull
        private Long id ;

        @ApiModelProperty(value = "인증 번호")
        @NotEmpty
        private String authNo;

        @ApiModelProperty(value = "핸드폰 번호")
        @NotEmpty
        private String phone;

        @ApiModelProperty(value = "인증타입 (JOIN,FIND_PASSWORD)")
        @Enumerated(EnumType.STRING)
        private Auth.AuthType authType;

    }

    @ApiModel(value = "비밀번호 변경 요청 정보")
    @Getter
    public static class ChangePasswordReq {
        @ApiModelProperty(value = "인증 토큰", required = true)
        private String authToken;

        @ApiModelProperty(value = "비밀번호", required = true)
        @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}", message = "패스워드는 대문자, 소문자를 포함하며 8-20자리로 구성되어야 합니다.")
        private String password;
    }
}
