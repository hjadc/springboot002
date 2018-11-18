package com.huju.core;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *直接使用明文密码(测试用的,实际场合不推荐)
 * Created by huju on 2018/11/18.
 */
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(charSequence.toString());
    }
}