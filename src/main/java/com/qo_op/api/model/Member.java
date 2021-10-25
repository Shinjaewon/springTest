package com.qo_op.api.model;

import com.qo_op.api.comm.SHA256;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column( unique = true)
    private String token;


    @CreatedDate
    private LocalDateTime lastLoginAt;

    @Builder
    public Member(String email, String nickName, String password, String userName, String phone) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
    }

    public boolean isPasswordValidation(String password) throws NoSuchAlgorithmException {
        return this.password.equals(new SHA256().encrypt(password));
    }


    public void login(String token) {
        this.token = token;
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
