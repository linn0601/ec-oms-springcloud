package org.linn;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.linn.util.GsonUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class AuthorizationApplicationTest {

    @Test
    public void contextLoad(){
        Date date = DateUtils.addDays(new Date(), 1);
        System.out.println(GsonUtils.getGson().toJson(date));
        System.out.println(1);
    }


}
