package com.cs.parking;

import com.cs.parking.Netty.WebsocketServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@MapperScan("com.cs.parking.mapper")
@EnableSwagger2
@SpringBootApplication
@Slf4j
//开启mybatis事务支持
@EnableTransactionManagement
public class ParkingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
    }

    @Autowired
    WebsocketServer websocketServer;

    ExecutorService executorService = Executors.newFixedThreadPool(1);
    @Override
    public void run(String... args) throws Exception {
        executorService.execute(() -> {
            try {
                websocketServer.startup();
            } catch (Exception e) {
                log.error("ws app", e);
            }
        });
    }
}
