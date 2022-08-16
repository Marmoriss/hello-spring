package com.kh.spring.board.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {
	@NonNull
	private int no;
	@NonNull
	private String title;
	private String memberId;
	private String content;
	private int readCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
