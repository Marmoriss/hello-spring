package com.kh.spring.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
@SessionAttributes({"loginMember"})
public class MemberController {

//	static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
//	@RequestMapping(path = "/memberEnroll.do", method = RequestMethod.GET)
	@GetMapping("/memberEnroll.do")
	public String memberEnroll() {
		
		return "/member/memberEnroll";
	}
	
	/**
	 * $2a$10$2.AdYu08nfVhU89v8PyfsuF0kObCQvGCdJiR3I5p1dSQMY81FfD6O
	 * 
	 * - $2a$ 알고리즘타입
	 * - 10$ 옵션 (비용이 높을수록 속도가 오래걸리고, 메모리사용량이 많다)
	 * - 2.AdYu08nfVhU89v8Pyfsu 22자리 random salt
	 * - F0kObCQvGCdJiR3I5p1dSQMY81FfD6O 31자리 hashing + encoding처리
	 * 
	 */
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		try {
			log.debug("member = {}", member);
			
			// 비밀번호 암호화
			String rawPassword = member.getPassword();
			String encodedPassword = bcryptPasswordEncoder.encode(rawPassword);
			member.setPassword(encodedPassword);
			log.debug("encodedPassword = {}" + encodedPassword);
			
			int result = memberService.insertMember(member);
			redirectAttr.addFlashAttribute("msg", "회원가입이 정상적으로 처리되었습니다.");
		} catch (Exception e) {
			log.error("회원 등록 오류 : " + e.getMessage(), e);
			throw e;
		}
		return "redirect:/";
	}
	
	/**
	 * viewName이 null인 경우, 요청 url을 기준으로 jsp위치를 추론한다.
	 * 
	 * /member/memberLogin.do
	 * -> member/memberLogin
	 * 
	 */
	@GetMapping("/memberLogin.do")
	public void memberLogin() {
		
	}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(@RequestParam String memberId,
							@RequestParam String password,
							Model model,
							RedirectAttributes redirectAttr) {
		
		// 1. memberId로 회원 조회
		Member member = memberService.selectOneMember(memberId);
		log.debug("member = {}", member);
		
		String location = "/";
		// 2. member가 null이 아니면서 password가 일치하면 로그인 성공
		if(member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
			// model을 통해 session scope에 속성 저장 : 클래스 레벨에 @SessionAttributes로 등록
			model.addAttribute("loginMember", member);
		} else {
			// 로그인 실패
			redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			location ="/member/memberLogin.do";
		}
		return "redirect:" + location;
	}
	
	/**
	 * @SessionAttribute로 세션관리를 한다면, SessionStatus#setComplete()로 만료처리해야한다.
	 */
	@GetMapping("/memberLogout.do")
	public String memberLogout(SessionStatus sessionStatus) {
		
		if(!sessionStatus.isComplete())
			sessionStatus.setComplete();
		
		return "redirect:/";
	}
	
	@GetMapping("/memberDetail.do")
	public ModelAndView memberDetail(ModelAndView mav, @SessionAttribute Member loginMember) {
		
		log.debug("loginMember = {}", loginMember);
		mav.setViewName("member/memberDetail");
		return mav;
	}
	
	@PostMapping("/memberUpdate.do")
	public String memberUpdate(@ModelAttribute Member member, RedirectAttributes redirectAttr, Model model) {
		log.debug("member = {}", member);
		int result = memberService.updateMember(member);
		
		model.addAttribute("loginMember", memberService.selectOneMember(member.getMemberId()));
		redirectAttr.addFlashAttribute("msg", "회원 정보 수정 완료!");
		
		return "redirect:/member/memberDetail.do";
	}
	
	
	
	
	
	
	
	
	
	
	
}
