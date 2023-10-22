package org.linn.feign;

import org.linn.vo.JwtToken;
import org.linn.vo.UsernamePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class OpenFeignController {

    @Autowired
    private AuthorityFeignClient authorityFeignClient;

    @GetMapping("/get_town")
    public JwtToken getTown(){
        UsernamePassword usernamePassword = new UsernamePassword();
        usernamePassword.setUsername("linn");
        usernamePassword.setPassword("123");

        return authorityFeignClient.token(usernamePassword);
    }
}
