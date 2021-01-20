package cn.machine.geek.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: MachineGeek
 * @Description: Token过滤器
 * @Email: 794763733@qq.com
 * @Date: 2021/1/19
 */
//@Component
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.isEmpty(token)){
            token = token.replace("bearer ","");
            log.info(token);
            /*try {
                JWSObject parse = JWSObject.parse(token);
                JWSVerifier jwsVerifier = new RSASSAVerifier();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
//            request.getHeaders().set("info",);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
