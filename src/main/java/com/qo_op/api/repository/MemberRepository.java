package com.qo_op.api.repository;

import com.qo_op.api.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByNickNameOrEmailOrPhone(String loginName, String loginName1, String loginName2);

    Optional<Member> findByToken(String token);

    Member findByPhone(String phone);
}
