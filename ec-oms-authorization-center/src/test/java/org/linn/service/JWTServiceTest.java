package org.linn.service;

import org.junit.jupiter.api.Test;
import org.linn.util.GsonUtils;
import org.linn.util.TokenParseUtil;
import org.linn.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTServiceTest {

    private final static Logger logger = LoggerFactory.getLogger(JWTServiceTest.class);

    @Autowired
    private JWTService jwtService;

    @Test
    public void testGenerateAndParseToken() throws Exception {
        String jwtToken = jwtService.generateToken("linn", "123");

        logger.info("jwt token：{}", jwtToken);
        UserInfo userInfo = TokenParseUtil.parseUserInfoToken(jwtToken);
        logger.info("user info： {}", GsonUtils.getGson().toJson(userInfo));
    }
}
