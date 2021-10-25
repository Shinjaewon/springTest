package com.qo_op.api.comm;

import com.qo_op.api.model.Member;

public class UserTokenStore {
    private static final ThreadLocal<Member> threadLocal;

    static {
        threadLocal = new ThreadLocal<>();
    }

    public static void set(Member member) {
        threadLocal.set(member);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static Member get() {
        return threadLocal.get();
    }
}
