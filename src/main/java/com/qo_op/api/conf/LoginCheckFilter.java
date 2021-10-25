package com.qo_op.api.conf;

import com.qo_op.api.comm.UserTokenStore;
import com.qo_op.api.model.Member;
import com.qo_op.api.repository.MemberRepository;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginCheckFilter implements javax.servlet.Filter {

    private MemberRepository memberRepository;

    public LoginCheckFilter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        if(token == null) {
            ((HttpServletResponse) response).setStatus(401);
            response.getOutputStream().write("API Key is missing!".getBytes());
            return;
        }
        Optional<Member> memberOptional = memberRepository.findByToken(token);
        if(!memberOptional.isPresent()) {
            ((HttpServletResponse) response).setStatus(403);
            response.getOutputStream().write("API Key is invalid".getBytes());
            return;
        }else{
            UserTokenStore.set(memberOptional.get());
        }


        chain.doFilter(request, response);
    }
}
