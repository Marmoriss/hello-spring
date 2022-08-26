package com.kh.spring.chat.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.chat.model.dao.ChatDao;
import com.kh.spring.chat.model.dto.ChatMember;

import lombok.NonNull;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	@Autowired
	ChatDao chatDao;
	
	@Override
	public ChatMember findChatMemberByMemberId(@NonNull String memberId) {
		return chatDao.findChatMemberByMemberId(memberId);
	}
	
	/**
	 * 두 명의 회원 등록에 대해 트랜잭션 처리
	 */
	@Override
	public void insertChatMembers(List<ChatMember> chatMembers) {
		for(ChatMember chatMember : chatMembers) {
			chatDao.insertChatMember(chatMember);
		}
		
	}
	
	
	
	
	
	
}
