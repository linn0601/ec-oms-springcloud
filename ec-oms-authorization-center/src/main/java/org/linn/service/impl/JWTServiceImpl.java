package org.linn.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.linn.constant.PrivateKeyConstant;
import org.linn.entity.User;
import org.linn.rsa.PublicKeyConstant;
import org.linn.service.JWTService;
import org.linn.service.UserRepository;
import org.linn.util.GsonUtils;
import org.linn.vo.UserInfo;
import org.linn.vo.UsernamePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class JWTServiceImpl implements JWTService {

    private static final Logger logger = LoggerFactory.getLogger(JWTServiceImpl.class);

    private final UserRepository userRepository;

    public JWTServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user == null) {
            logger.error("can not find user: [{}], [{}]", username, password);
            return null;
        }
        UserInfo userInfo = buildUserInfo(user);
        expire = expire <= 0 ? PrivateKeyConstant.DEFAULT_EXPIRE_DAY : expire;
        // 计算超时时间
        Date expireDate = DateUtils.addDays(new Date(), expire);
        return Jwts.builder()
                // jwt payload -> KV
                .claim(PublicKeyConstant.JWT_USER_INFO_KEY, GsonUtils.getSimpleGson().toJson(userInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
    }

    private static UserInfo buildUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        return userInfo;
    }

    @Override
    public String registerUserAndGenerateToken(UsernamePassword usernamePassword) throws Exception {

        // 校验用户名是否重复存在
        User user = userRepository.findByUsername(usernamePassword.getUsername());

        if (user != null) {
            logger.info("");
            return null;
        }

        User newUser = new User();
        newUser.setUsername(usernamePassword.getUsername());
        newUser.setPassword(usernamePassword.getPassword());

        userRepository.save(newUser);

        // 生成token 返回
        return generateToken(newUser.getUsername(), newUser.getPassword());
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                Base64.getDecoder().decode(PrivateKeyConstant.PRIVATE_KEY)
        );

        KeyFactory keyFactory = KeyFactory.getInstance(PublicKeyConstant.ENCRYPTION_ALGORITHM_RSA);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }
}
