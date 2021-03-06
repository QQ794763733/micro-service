package cn.machine.geek.controller;

import cn.machine.geek.common.R;
import cn.machine.geek.dto.AuthorityTree;
import cn.machine.geek.entity.Authority;
import cn.machine.geek.service.AuthorityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: MachineGeek
 * @Description: 权力控制器
 * @Email: 794763733@qq.com
 * @Date: 2021/1/7
 */
@RestController
@RequestMapping("/authority")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private TokenStore tokenStore;

    /**
     * @param
     * @Author: MachineGeek
     * @Description: 获取权力树
     * @Date: 2021/1/11
     * @Return: cn.machine.geek.common.R
     */
    @GetMapping("/tree")
    public R tree() {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Authority::getSort);
        return R.ok(getChild(0L, authorityService.list(queryWrapper)));
    }

    /**
     * @param
     * @Author: MachineGeek
     * @Description: 获取当前用户的权限
     * @Date: 2021/1/11
     * @Return: cn.machine.geek.common.R
     */
    @GetMapping("/getMyAuthorities")
    public R getMyAuthorities(OAuth2Authentication oAuth2Authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
        OAuth2AccessToken token = tokenStore
                .readAccessToken(oAuth2AuthenticationDetails.getTokenValue());
        Long id = (Long) token.getAdditionalInformation().get("id");
        // 获取当前用户的所有权限
        List<Authority> authorities = authorityService.listByAccountId(id);
        // 把权限分为路由权限和API权限
        List<Authority> routes = new ArrayList<>();
        List<Authority> apis = new ArrayList<>();
        authorities.forEach((authority) -> {
            if (authority.getUri() == null) {
                apis.add(authority);
            } else {
                routes.add(authority);
            }
        });
        // 返回权限
        Map<String, Object> map = new HashMap<>();
        map.put("apis", apis);
        // 返回路由树
        map.put("routes", getChild(0L, routes));
        return R.ok(map);
    }

    /**
     * @param id
     * @param authorities
     * @Author: MachineGeek
     * @Description: 构建权限树
     * @Date: 2021/1/11
     * @Return: java.util.List<cn.machine.geek.dto.AuthorityTreeNode>
     */
    private List<AuthorityTree> getChild(Long id, List<Authority> authorities) {
        List<AuthorityTree> child = new ArrayList<>();
        authorities.forEach((authority) -> {
            if (authority.getPid().equals(id)) {
                AuthorityTree authorityTree = new AuthorityTree();
                BeanUtils.copyProperties(authority, authorityTree);
                authorityTree.setChild(getChild(authorityTree.getId(), authorities));
                child.add(authorityTree);
            }
        });
        return child;
    }
}