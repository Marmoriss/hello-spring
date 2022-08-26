package com.kh.spring.menu.model.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

	public static final String MENU_BASE_URL = "http://localhost:10000/springboot/menu";
	
	/**
	 * Resource
	 * 다음 구현체들의 추상화레이어를 제공한다.
	 * 
	 * - 웹상 자원 UrlResource => http://
	 * - classpath 자원 ClassPathResource
	 * - 서버 컴퓨터 자원 FileSystemResource => file:
	 * - ServletContext (web root)자원 ServletContextResource
	 * - 입출력 자원 InputStreamResource
	 * - 이진데이터 자원 ByteArrayResource
	 * @throws IOException 
	 * 
	 * @ResponseBody - 핸들러의 반환된 Java 객체를 응답메세지 바디에 직접 출력하는 경우
	 * 
	 */
	
	@Autowired
	ResourceLoader resourceLoader;
	
	public Resource selectMenuList() {
		return resourceLoader.getResource(MENU_BASE_URL); // UrlResource 객체
	}

	public Resource findByType(String type) {
		return resourceLoader.getResource(MENU_BASE_URL + "/type/" + type);
	}

	public Resource findByTypeAndTaste(String type, String taste) {
		return resourceLoader.getResource(MENU_BASE_URL + "/type/" + type + "/taste/" + taste);
	}

	
}
