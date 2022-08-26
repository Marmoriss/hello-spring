package com.kh.spring.chat.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.kh.spring.chat.model.dto.ChatMember;

import lombok.NonNull;

public interface ChatDao {

	@Select("select * from chat_member where member_id = #{memberId}")
	ChatMember findChatMemberByMemberId(@NonNull String memberId);

	@Insert("insert into chat_member values(#{chatroomId}, #{memberId}, default, default, null)")
	void insertChatMember(ChatMember chatMember);

	
	
	
	
	
	
	
	
	
	
	
}
