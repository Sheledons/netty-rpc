package com.example.client;

import com.example.client.service.HelloService;
import com.example.entity.User;
import com.sheledon.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;

/**
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
@RpcScan(basePackages = {"com.example.client"})
public class Main {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        HelloService helloService = context.getBean("helloService", HelloService.class);
        Thread.sleep(2000);
        CountDownLatch latch = new CountDownLatch(1);
        helloService.show(1);
        User user = User.builder().id(1).username("张三").age(10).build();
        // 保存
        helloService.save(user);
        helloService.show(1);
        // 更新
        user.setUsername("小明");
        helloService.update(user);
        helloService.show(1);
        // 删除
        helloService.delete(1);
        helloService.show(1);

        latch.await();
    }
}
