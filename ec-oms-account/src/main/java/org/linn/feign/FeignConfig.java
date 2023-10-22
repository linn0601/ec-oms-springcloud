package org.linn.feign;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLogger(){
        return Logger.Level.FULL; // 修改日志级别为debug
    }

    /**
     * 开启重拾
     * period 发起间隔
     * max period 最大间隔
     * max attempts 最多请求次数
     */
    public Retryer feignRetryer(){
        return new Retryer.Default(
                100,
                1000,
                3
        );
    }

    public static final int CONNECT_TIMEOUT_MILLS = 5000;
    public static final  int READ_TIMEOUT_MILLS = 5000;

    /**
     * 请求链接和响应时间进行限制
     */
    @Bean
    public Request.Options options(){
        return new Request.Options(
                CONNECT_TIMEOUT_MILLS , TimeUnit.MILLISECONDS,
                READ_TIMEOUT_MILLS, TimeUnit.MILLISECONDS,
                true
        );
    }


}
