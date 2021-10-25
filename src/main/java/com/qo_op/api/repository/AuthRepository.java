package com.qo_op.api.repository;

import com.qo_op.api.model.Auth;
import com.qo_op.api.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends CrudRepository<Auth, Long> {
    Optional<Auth> findByToken(String authToken);

    Optional<Auth> findByTokenAndAuthType(String authToken, Auth.AuthType authType);
}
