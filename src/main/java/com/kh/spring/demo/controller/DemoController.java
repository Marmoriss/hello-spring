package com.kh.spring.demo.controller;


import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.demo.model.dto.Dev;
import com.kh.spring.demo.model.dto.Gender;
import com.kh.spring.demo.model.service.DemoService;

/**
 * <pre>
 * @Controller 클래스의 handler메소드가 가질수 있는 매개변수 타입
 * 
 * HttpServletRequest
 * HttpServletResponse
 * HttpSession
 * 
 * java.util.Locale : 요청에 대한 Locale
 * InputStream/Reader : 요청에 대한 입력스트림
 * OutputStream/Writer : 응답에 대한 출력스트림. ServletOutputStream, PrintWriter
 * 
 * 사용자입력값처리
 * Command객체 : http요청 파라미터를 커맨드객체에 저장한 VO객체
 * CommandMap :  HandlerMethodArgumentResolver에 의해 처리된 사용자입력값을 가진 Map객체
 * @Valid : 커맨드객체 유효성 검사객체
 * Error, BindingResult : Command객체에 저장결과(Command객체 바로 다음위치시킬것.)
 * @PathVariable : 요청url중 일부를 매개변수로 취할 수 있다.
 * @RequestParam : 사용자입력값을 자바변수에 대입처리(필수여부 설정)
 * @RequestHeader : 헤더값
 * @CookieValue : 쿠키값
 * @RequestBody : http message body에 작성된 json을 vo객체로 변환처리
 * 
 * 뷰에 전달할 모델 데이터 설정
 * ModelAndView
 * ModelMap 
 * Model
 
 * @ModelAttribute : model속성에 대한 getter
 * @SessionAttribute : session속성에 대한 getter(required여부 선택가능)
 * @SessionAttributes : session에서 관리될 속성명을 class-level에 작성
 * SessionStatus: @SessionAttributes로 등록된 속성에 대하여 사용완료(complete)처리. 세션을 폐기하지 않고 재사용한다.
 * 
 * 기타
 * MultipartFile : 업로드파일 처리 인터페이스. CommonsMultipartFile
 * RedirectAttributes : DML처리후 요청주소 변경을 위한 redirect시 속성처리 지원
 *
 * </pre>
 */

@Controller
@RequestMapping("/demo")
public class DemoController {
	
	static final Logger log = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	private DemoService demoService;
	
	
	/**
	 * value : path에 대한 별칭
	 * method : 전송 방식, 작성하지 않으면 모든 전송 방식을 허용
	 */
	@RequestMapping(value = "/devForm.do", method = RequestMethod.GET)
	public String devForm() {
		
		log.info("{} 요청!", "/demo/devForm.do");
		return "/demo/devForm";
	}
	
	@RequestMapping(path = "/dev1.do", method = RequestMethod.POST)
	public String dev1(HttpServletRequest request, HttpServletResponse response) {
		
		String name = request.getParameter("name");
		int career = Integer.parseInt(request.getParameter("career"));
		String email = request.getParameter("email");
		String _gender = request.getParameter("gender");
		Gender gender = _gender != null ? Gender.valueOf(_gender) : null;
		String[] lang = request.getParameterValues("lang");
		
		Dev dev = new Dev(0, name, career, email, gender, lang, LocalDateTime.now());
		
		log.info("dev = {}", dev);
		
		request.setAttribute("dev", dev);
		
		return "demo/devResult";
	}
	
	/**
	 * @RequestParam
	 * - 모든 필드는 필수값이다. (기본값)
	 * - required = false (옵션으로 처리)
	 * - 자바변수명, name 속성값과 일치하는 사용자 입력값을 찾는다
	 * - 기본값 처리도 가능하다. (defaultValue = "")
	 */
	@RequestMapping(path = "/dev2.do", method = RequestMethod.POST)
	public String dev2(
			@RequestParam(required = false, defaultValue = "홍길동") String nm,
//			@RequestParam(name = "name") String name,
			
			@RequestParam int career,
			@RequestParam String email,
			@RequestParam Gender gender,
			@RequestParam String[] lang,
			Model model) {
		
		Dev dev = new Dev(0, nm, career, email, gender, lang, LocalDateTime.now());
		log.info("dev = {}", dev);
			
		// view단에 전달할 데이터를 model에 속성으로 추가 -> request scope에 속성으로 저장
		model.addAttribute("dev", dev);
		return "demo/devResult";
	}
	
	/**
	 * 커맨드 객체
	 * - 사용자 입력 name 값 - property(setter)가 일치하면 값대입
	 * - 자동으로 model속성으로 등록된다.
	 * 
	 */
	@RequestMapping(path = "/dev3.do", method = RequestMethod.POST)
	public String dev3(Dev dev) {
		dev.setCreatedAt(LocalDateTime.now());
		log.info("dev = {}", dev);
		return "demo/devResult";
	}
	
	@RequestMapping(path = "/insertDev.do", method = RequestMethod.POST)
	public String insertDev(Dev dev, RedirectAttributes redirectAttr) {
		
		int result = demoService.insertDev(dev);
		redirectAttr.addFlashAttribute("msg", "개발자 등록 성공");
		return "redirect:/";
	}
	
	@RequestMapping(path = "/devList.do", method = RequestMethod.GET)
	public String devList(Model model) {
		List<Dev> list = demoService.selectDevList();
		log.info("list= {}", list);
		model.addAttribute("list", list);
		return "demo/devList";
	}
	
	@RequestMapping(path = "/updateDev.do", method = RequestMethod.GET)
	public String updateDevList(@RequestParam int no, Model model) {
		Dev updateDev = demoService.selectDevByNo(no);
		log.info("updateDev = {}", updateDev);
		model.addAttribute("updateDev", updateDev);
		
		return "demo/devUpdateForm";
	}
	
	@RequestMapping(path = "/updateDev.do", method = RequestMethod.POST)
	public String updateDev(Dev dev, RedirectAttributes redirectArrt) {
		int result = demoService.updateDev(dev);
		log.info("result = {}", result);
		redirectArrt.addFlashAttribute("msg", "개발자 정보 수정 성공");
		
		return "redirect:/";
	}
	
	@RequestMapping(path = "/deleteDev.do", method = RequestMethod.POST)
	public String deleteDev(@RequestParam int no, RedirectAttributes redirectArrt) {
		
		int result = demoService.deleteDev(no);
		log.info("result = {}", result);
		redirectArrt.addFlashAttribute("msg", "개발자 정보 삭제 성공");
		
		return "redirect:/";
	}
	
	
	
	
	
	
	
}

















