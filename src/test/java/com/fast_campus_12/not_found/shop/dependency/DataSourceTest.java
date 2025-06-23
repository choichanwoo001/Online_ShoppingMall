package com.fast_campus_12.not_found.shop.dependency;

import com.fast_campus_12.not_found.shop.config.MyBatisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DataSourceTest {

    @Test
    void testDataSourceConnection() throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyBatisConfig.class);

        DataSource dataSource = context.getBean(DataSource.class);

        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
            System.out.println("✅ 연결 성공: " + conn);
        } catch (Exception e) {
            fail("❌ 연결 실패: " + e.getMessage());
        } finally {
            context.close();
        }
    }
}

