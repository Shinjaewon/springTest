package com.qo_op.api.model;

import com.qo_op.api.dto.AccountDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "auth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    private String authNo;

    private String phone;

    private String token;

    private LocalDateTime expiredAt;

    public boolean isValid(AccountDto.AuthReq dto) {
        if(!this.phone.equals(dto.getPhone())){
            return false;
        }
        if(!this.authNo.equals(dto.getAuthNo())){
            return false;
        }
        if(!this.authType.equals(dto.getAuthType())){
            return false;
        }
        return true;
    }

    public void updateToken(String token) {
        this.token = token;
        this.expiredAt = LocalDateTime.now().plusMinutes(3);

    }

    public enum AuthType {
        JOIN,FIND_PASSWORD
    }

    @Builder
    public Auth(String phone, AuthType authType) {
        this.authType = authType;
        this.authNo = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        this.phone = phone;
    }

}
