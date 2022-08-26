package com.kh.spring.member.model.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Member extends MemberEntity implements UserDetails {

	/**
	 * SimpleGrantedAuthority
	 * - 문자열로 권한을 관리
	 * - "ROLE_USER" -> new SimpleGrantedAuthority("ROLE_USER")
	 */
	private List<SimpleGrantedAuthority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return memberId;
	}

	@Override
	public boolean isAccountNonExpired() {
		// enabled나 true를 반환
		return enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		// enabled나 true를 반환
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// enabled나 true를 반환
		return enabled;
	}

}
