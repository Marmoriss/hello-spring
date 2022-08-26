package com.kh.spring.data.model.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kh.spring.data.model.dto.corona19.Response;
import com.kh.spring.data.model.dto.course.Course;
import com.kh.spring.data.model.dto.course.Student;
import com.kh.spring.data.model.exception.DataProcessingException;

@Service
public class DataServiceImpl implements DataService {

	private static final String XML_COURSE_URL = "https://shqkel.github.io/course.xml";
	private static final String JSON_COURSE_URL = "https://shqkel.github.io/course.json";
	private static final String CORONA_URL = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";
	private static final String CORONA_SERVICE_KEY = "xG7FbVRiB%2FVRULA5s7YlsBsZOsoP1a%2BS%2BdHFBLAaDX%2FRj3QgOTytvSMvI7EbGXFYK0yIDMpKM97N9%2F9awD9dYg%3D%3D";
	
	/**
	 * 1. org.w3c.dom.Document (html 제어와 비슷)
	 * 2. JAXB (xml 처리를 지원하는 java 모듈) -- 안 배움
	 * 3. jackson XmlMapper (jackson-dataformat-xml 의존)
	 */
	
	/**
	 * org.w3c.dom.Document 방식
	 */
	@Override
	public Course getXmlCourse() {
		Course course = null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(XML_COURSE_URL);
			
			// 정규화 : textNode 정리 (생략 가능)
			doc.getDocumentElement().normalize();
			long id = Long.parseLong(doc.getElementsByTagName("id").item(0).getTextContent());
			String title = doc.getElementsByTagName("title").item(0).getTextContent();
			int price = Integer.parseInt(doc.getElementsByTagName("price").item(0).getTextContent());
			LocalDate created = LocalDate.parse(doc.getElementsByTagName("created").item(0).getTextContent());
			
			NodeList nodeList = doc.getElementsByTagName("student");
			List<Student> students = new ArrayList<>();
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i); // <student></student>
				Element elem = (Element) node; // 자식태그를 찾으려면 element로 변환 해야함
				long _id = Long.parseLong(elem.getElementsByTagName("id").item(0).getTextContent());
				String name = elem.getElementsByTagName("name").item(0).getTextContent();
				String tel = elem.getElementsByTagName("tel").item(0).getTextContent();
				
				Student student = Student.builder()
										.id(_id)
										.name(name)
										.tel(tel)
										.build();
				students.add(student);
			}
			
			course = course.builder()
							.id(id)
							.title(title)
							.price(price)
							.created(created)
							.students(students)
							.build();
			
		} catch (Exception e) {
			throw new DataProcessingException(XML_COURSE_URL);
		}
		
		return course;
	}
	
	
	/**
	 * XmlMapper 방식
	 */
	@Override
	public Course getXmlCourse2() {
		ObjectMapper xmlMapper = new XmlMapper().registerModule(new JavaTimeModule());
		Course course = null;
		try {
			course = xmlMapper.readValue(new URL(XML_COURSE_URL), Course.class);
		} catch (IOException e) {
			throw new DataProcessingException(XML_COURSE_URL);
		}
		
		return course;
	}
	
	
	@Override
	public Course getJsonCourse() {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		Course course = null;
		try {
			course = objectMapper.readValue(new URL(JSON_COURSE_URL), Course.class);
		} catch (IOException e) {
			throw new DataProcessingException(JSON_COURSE_URL);
		}
		
		return course;
	}
	
	@Override
	public Response getCorona19Status(LocalDate date) {
		Response response = null;
		ObjectMapper xmlMapper = new XmlMapper();
		try {
			response = xmlMapper.readValue(urlFormatter(date), Response.class);
		} catch (IOException e) {
			throw new DataProcessingException(CORONA_URL);
		}
		
		return response;
	}


	private URL urlFormatter(LocalDate date) throws MalformedURLException {
		String url = CORONA_URL + "?serviceKey=" + CORONA_SERVICE_KEY
				+ "&numOfRows=20"
				+ "&startCreateDt=" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
				+ "&endCreateDt=" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		return new URL(url);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
