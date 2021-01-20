package cn.machine.geek.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * @Author: MachineGeek
 * @Description: 资源服务器配置
 * @Email: 794763733@qq.com
 * @Date: 2021/1/19
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;
    private static final String RESOURCE_ID = "app";

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
     * @Description: JWT转换器加载私钥和公钥
     * @Date: 2021/1/19
     * @param
     * @Return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 加载公钥
        ClassPathResource pubKey = new ClassPathResource("MachineGeek.pub");
        // 设置公钥
        try {
            String pubStr = new String(FileCopyUtils.copyToByteArray(pubKey.getInputStream()));
            converter.setVerifier(new RsaVerifier(pubStr));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return converter;
    }

    /**
    * @Author: MachineGeek
    * @Description: 配置资源服务器策略
    * @Date: 2021/1/19
     * @param resources
    * @Return: void
    */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId(RESOURCE_ID)
                .tokenStore(tokenStore)
                .stateless(true);
    }
}
