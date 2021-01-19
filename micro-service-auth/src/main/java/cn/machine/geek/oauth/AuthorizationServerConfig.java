package cn.machine.geek.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * @Author: MachineGeek
 * @Description: 认证服务器配置
 * @Email: 794763733@qq.com
 * @Date: 2021/1/18
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    // 认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;
    // 密码加密器
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 用户数据服务
    @Autowired
    private UserDetailsService userDetailsService;
    // 数据源
    @Autowired
    private DataSource dataSource;
    // 对称加密的秘钥
    private static final String SECRET = "MachineGeek";

    /**
     * @Author: MachineGeek
     * @Description: 注册JDBC客户端服务并配置密码加密器
     * @Date: 2021/1/18
     * @param
     * @Return: org.springframework.security.oauth2.provider.client.JdbcClientDetailsService
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService(){
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
    * @Author: MachineGeek
    * @Description: 配置JDBC授权码
    * @Date: 2021/1/19
     * @param
    * @Return: org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
    */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * @Author: MachineGeek
     * @Description: 注册JWT存储
     * @Date: 2021/1/18
     * @Return: org.springframework.security.oauth2.provider.token.TokenStore
     */
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
    * @Author: MachineGeek
    * @Description: JWT转换器
    * @Date: 2021/1/19
     * @param
    * @Return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
    */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SECRET);
        return converter;
    }

    /**
     * @Author: MachineGeek
     * @Description: 配置客户端服务
     * @Date: 2021/1/18
     * @param clients
     * @Return: void
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 客户端服务
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
    * @Author: MachineGeek
    * @Description: 配置认证管理器、JDBC授权码、用户服务、JWT存储策略、JWT转换器
    * @Date: 2021/1/18
     * @param endpoints
    * @Return: void
    */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 认证管理器
        endpoints.authenticationManager(authenticationManager)
                // 授权码服务
                .authorizationCodeServices(authorizationCodeServices())
                // 用户服务
                .userDetailsService(userDetailsService)
                // Token存储策略为JWT
                .tokenStore(tokenStore())
                // JWT转换器
                .accessTokenConverter(accessTokenConverter());
    }

    /**
    * @Author: MachineGeek
    * @Description: 配置时授权URI的安全约束
     * /oauth/authorize:授权端点。
     * /oauth/token:令牌端点。
     * /oauth/confirm_access:用户确认授权提交端点。
     * /oauth/error:授权服务错误信息端点。
     * /oauth/check_token:用于资源服务访问的令牌解析端点。
     * /oauth/token_key:提供公有密匙的端点，如果你使用JWT令牌的话
    * @Date: 2021/1/18
     * @param security
    * @Return: void
    */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }
}