package com.kh.spring.board.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Board extends BoardEntity {

	private int attachCount;

	public Board(@NonNull int no, @NonNull String title, String memberId, String content, int readCount,
			LocalDateTime createdAt, LocalDateTime updatedAt, int attachCount) {
		super(no, title, memberId, content, readCount, createdAt, updatedAt);
		this.attachCount = attachCount;
	}
	
	
	
}