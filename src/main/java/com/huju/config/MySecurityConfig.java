package com.huju.config;

import com.huju.core.MyPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security安全认证模块的配置
 * @EnableWebSecurity已经带上了@Configuration注解
 *
 * Created by huju on 2018/11/18.
 */
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * 定制授权规则
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);

        // 定制请求的授权规则
        http.authorizeRequests().antMatchers("/").permitAll() // 主页放行
                .antMatchers("/level1/**").hasRole("VIP1")    // 需要VIP1角色才能访问
                .antMatchers("/level2/**").hasRole("VIP2")    // 需要VIP2角色才能访问
                .antMatchers("/level3/**").hasRole("VIP3");    // 需要VIP3角色才能访问

        /**
         * 开启自动配置的登陆功能
         * 1./login来到登陆页
         * 2.重定向到/login?erroe表示登陆失败
         * 3.还有更多细节查看官方文档
         */
        http.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/userlogin");

        /**
         * 开启自动配置的注销功能
         * 1.访问logout 表示用户注销,并清空session
         * 2.注销成功会返回 /login?logout 页面
         */
        http.logout().logoutSuccessUrl("/"); // 定制注销成功后跳转的页面

        /**
         * 开启记住我功能
         * 登陆成功后,将cookie发给浏览器
         */
        http.rememberMe().rememberMeParameter("remeber");
    }

    /**
     * 定制认证规则
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // super.configure(auth);

        // 从内存获取定义好的用户和角色(也可以从数据库获取),注意必须对密码加密(这里用的是自己写的明文密码)
        auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder())
                .withUser("zhangsan").password("123456").roles("VIP1","VIP2")
                .and()
                .withUser("lisi").password("123456").roles("VIP2","VIP3")
                .and()
                .withUser("wangwu").password("123456").roles("VIP1","VIP3");

    }

}
