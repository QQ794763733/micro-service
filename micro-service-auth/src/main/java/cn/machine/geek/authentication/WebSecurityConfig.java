package cn.machine.geek.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
* @Author: MachineGeek
* @Description: 安全配置类
* @Email: 794763733@qq.com
* @Date: 2020/10/3
*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** @Author: MachineGeek
     * @Description: 配置认证路径
     * @Date: 2020/10/3
     * @param http
     * @Return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置表单登录
        http
                // 关闭Session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 设置注销路径
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
                // 设置其余请求全部拦截
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                // 开启跨域 关闭CSRF攻击
                .cors().and()
                .csrf().disable();
    }
}
