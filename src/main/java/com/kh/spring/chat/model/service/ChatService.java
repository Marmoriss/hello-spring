package com.kh.spring.chat.model.service;

import java.util.List;

import com.kh.spring.chat.model.dto.ChatMember;

import lombok.NonNull;

public interface ChatService {

	ChatMember findChatMemberByMemberId(@NonNull String memberId);

	void insertChatMembers(List<ChatMember> chatMembers);

}
