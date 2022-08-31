package com.kh.spring.chat.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.chat.model.dto.ChatLog;
import com.kh.spring.chat.model.dto.ChatMember;
import com.kh.spring.chat.model.service.ChatService;
import com.kh.spring.member.model.dto.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	/**
	 * 1. 채팅방 유무 조회
	 * 
	 * 2.a 처음 입장한 경우
	 * - 채팅방 아이디 생성 (8자리 정도의 문자열 token)
	 * 
	 * 2.b 재입장인 경우
	 * - 기존 채팅방 아이디 사용
	 */
	@GetMapping("/chat.do")
	public void chat(Authentication authentication, Model model) {
		// 1. 채팅방 유무 조회
		Member member = (Member) authentication.getPrincipal();
		ChatMember chatMember = chatService.findChatMemberByMemberId(member.getMemberId());
		log.debug("chatMember = {}", chatMember);
		
		String chatroomId = null;
		if(chatMember == null) {
			// 처음 입장한 경우
			chatroomId = generateChatroomId();
			log.debug("chatroomId = {}", chatroomId);
			
			// chatmember insert 2행 (사용자, 관리자)
			List<ChatMember> chatMembers = Arrays.asList(
					new ChatMember(chatroomId, member.getMemberId()),
					new ChatMember(chatroomId, "admin"));
			
			chatService.insertChatMembers(chatMembers);
			
		} else {
			// 재입장한 경우
			chatroomId =  chatMember.getChatroomId();
			List<ChatLog> chatLogs = chatService.findChatLogByChatroomId(chatroomId);
			log.debug("chatLogs = {}", chatLogs);
			model.addAttribute("chatLogs", chatLogs);
		}
		
		model.addAttribute("chatroomId", chatroomId);
	}
	
	/**
	 * 대문자 / 소문자 / 숫자 조합
	 * @return
	 */
	private String generateChatroomId() {
		// 대소문자, 숫자 8자리
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		
		final int len = 8;
		
		for(int i = 0; i < len; i++) {
			if(random.nextBoolean()) {
				// 영문자
				if(random.nextBoolean()) {
					// 대문자
					sb.append((char) random.nextInt(26) + 'A');
				} else {
					// 소문자
					sb.append((char) random.nextInt(26) + 'a');
				}
			} else {
				// 숫자
				sb.append(random.nextInt(10));
			}
		}
		
		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
