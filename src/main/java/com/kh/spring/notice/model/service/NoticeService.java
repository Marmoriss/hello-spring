package com.kh.spring.notice.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dto.Board;
import com.kh.spring.ws.payload.Payload;

@Service
public class NoticeService {

	@Autowired
	SimpMessagingTemplate simpMessageTemplate;
	
	public void sendNotice(Board board) {
		Payload payload = Payload.builder()
							.to(board.getMemberId()) // 글쓴이
							.msg("[" + board.getTitle() + "]을 누군가 조회했어요👀")
							.time(System.currentTimeMillis())
							.build();
		simpMessageTemplate.convertAndSend("/app/notice/" + board.getMemberId(), payload); 
	}
	
}
