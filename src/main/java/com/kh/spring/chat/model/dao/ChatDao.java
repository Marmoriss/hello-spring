package com.kh.spring.chat.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.kh.spring.chat.model.dto.ChatLog;
import com.kh.spring.chat.model.dto.ChatMember;

import lombok.NonNull;

public interface ChatDao {

	@Select("select * from chat_member where member_id = #{memberId}")
	ChatMember findChatMemberByMemberId(@NonNull String memberId);

	@Insert("insert into chat_member values(#{chatroomId}, #{memberId}, default, default, default)")
	int insertChatMember(ChatMember chatMember);

	@Insert("insert into chat_log values(seq_chat_log_no.nextval, #{chatroomId}, #{memberId}, #{msg}, #{time})")
	int insertChatLog(ChatLog chatLog);

	@Select("select * from chat_log where chatroom_id = #{chatroomId} order by no")
	List<ChatLog> findChatLogByChatroomId(String chatroomId);

	@Select(" select \r\n"
			+ "    no, \r\n"
			+ "    chatroom_id,\r\n"
			+ "    (select member_id from chat_member where chatroom_id = cl.chatroom_id and member_id != 'admin') member_id, -- 채팅방에 관리자가 아닌 회원아이디\r\n"
			+ "    msg,\r\n"
			+ "    time\r\n"
			+ " from(\r\n"
			+ "     select \r\n"
			+ "        cl.*,\r\n"
			+ "        row_number() over(partition by chatroom_id order by no desc) rnum\r\n"
			+ "     from\r\n"
			+ "        chat_log cl) cl\r\n"
			+ "where\r\n"
			+ "    rnum = 1"
			+ "order by\r\n"
		    + "		time desc")
	List<ChatLog> findRecentChatLogs();

	

	
	
	
	
	
	
	
	
	
}
