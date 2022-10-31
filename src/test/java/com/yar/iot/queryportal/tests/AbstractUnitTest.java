package com.yar.iot.queryportal.tests;

import com.yar.iot.queryportal.config.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(value = {SpringExtension.class})
@Import(TestConfig.class)
public abstract class AbstractUnitTest {
}
