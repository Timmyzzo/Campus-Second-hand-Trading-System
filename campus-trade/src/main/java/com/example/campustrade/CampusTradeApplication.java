// 这是正确的、适用于你当前项目结构的代码
package com.example.campustrade; // 注意：这里的包名是根据你的截图来的

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 之前的路径是 "com.cenzhihao.campus_trade.mapper"，是错误的
// 正确的路径应该是你项目里 mapper 包的完整路径
@MapperScan("com.example.campustrade.mapper")
public class CampusTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampusTradeApplication.class, args);
	}

}