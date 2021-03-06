package cn.machine.geek.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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
    // 自定义JWT内容
    @Autowired
    private CustomTokenEnhancer CustomTokenEnhancer;
    // RSA私钥文件名
    private static final String RSA_PRIVATE_KEY_FILE_NAME = "MachineGeek.jks";
    // RSA证书密码
    private static final String PASSWORD = "MachineGeek";

    /**
     * @param
     * @Author: MachineGeek
     * @Description: 注册JDBC客户端服务并配置密码加密器
     * @Date: 2021/1/18
     * @Return: org.springframework.security.oauth2.provider.client.JdbcClientDetailsService
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * @param
     * @Author: MachineGeek
     * @Description: 配置JDBC密码模式的授权码
     * @Date: 2021/1/19
     * @Return: org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * @Author: MachineGeek
     * @Description: 注册Token的JWT存储
     * @Date: 2021/1/18
     * @Return: org.springframework.security.oauth2.provider.token.TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * @param
     * @Author: MachineGeek
     * @Description: JWT转换器加载私钥
     * @Date: 2021/1/19
     * @Return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 加载私钥
        ClassPathResource preKey = new ClassPathResource(RSA_PRIVATE_KEY_FILE_NAME);
        // 设置私钥
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(preKey, PASSWORD.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(PASSWORD, PASSWORD.toCharArray()));
        return converter;
    }

    /**
     * @param
     * @Author: MachineGeek
     * @Description: Token增强链
     * @Date: 2021/1/19
     * @Return: org.springframework.security.oauth2.provider.token.TokenEnhancerChain
     */
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(CustomTokenEnhancer);
        tokenEnhancers.add(accessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        return tokenEnhancerChain;
    }

    /**
     * @param clients
     * @Author: MachineGeek
     * @Description: 配置客户端服务
     * @Date: 2021/1/18
     * @Return: void
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 客户端服务
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * @param endpoints
     * @Author: MachineGeek
     * @Description: 配置认证管理器、JDBC授权码、用户服务、JWT存储策略、Token增强链
     * @Date: 2021/1/18
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
                // Token增强链
                .tokenEnhancer(tokenEnhancerChain());
    }

    /**
     * @param security
     * @Author: MachineGeek
     * @Description: 配置时授权URI的安全约束
     * /oauth/authorize:授权端点。
     * /oauth/token:令牌端点。
     * /oauth/confirm_access:用户确认授权提交端点。
     * /oauth/error:授权服务错误信息端点。
     * /oauth/check_token:用于资源服务访问的令牌解析端点。
     * /oauth/token_key:提供公有密匙的端点，如果你使用JWT令牌的话
     * @Date: 2021/1/18
     * @Return: void
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }
}
