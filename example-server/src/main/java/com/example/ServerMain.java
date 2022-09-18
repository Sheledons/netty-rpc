package com.example;

import com.sheledon.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.SocketAddress;

/**
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
@RpcScan(basePackages = {"com.example"})
@ComponentScan(basePackages = {"com.example"})
public class ServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerMain.class);
    }
}
