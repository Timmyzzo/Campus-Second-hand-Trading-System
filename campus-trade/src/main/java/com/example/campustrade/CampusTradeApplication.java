// 启动器：整个后厨的总开关

package com.example.campustrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.campustrade.mapper") //扫描数据库操作接口

public class CampusTradeApplication {
	public static void main(String[] args) {
		SpringApplication.run(CampusTradeApplication.class, args);
	}
}