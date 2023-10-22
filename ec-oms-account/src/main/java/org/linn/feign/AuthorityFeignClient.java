package org.linn.feign;


import org.linn.vo.JwtToken;
import org.linn.vo.UsernamePassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// value = 向那个服务发起请求
@FeignClient(contextId = "authorityFeignClient",
        value = "ec-oms-authorization-center",
        path ="/ec-oms-authorization-center/auth")
public interface AuthorityFeignClient {


    @PostMapping(value = "/token", produces = "application/json", consumes = "application/json")
    JwtToken token(@RequestBody UsernamePassword usernamePassword);

}
