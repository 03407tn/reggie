package com.tn;

import com.tn.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ReggieTakeOutApplicationTests {

    @Autowired
    SMSUtils smsUtils;
    @Test
    void contextLoads() {


        smsUtils.sendSimpleMail("3156568410@qq.com","±êÌâ","ÄÚÈÝ");
        log.debug(String.valueOf(smsUtils));
    }

}
