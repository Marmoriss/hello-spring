package com.kh.spring.data.model.service;

import java.time.LocalDate;

import com.kh.spring.data.model.dto.corona19.Response;
import com.kh.spring.data.model.dto.course.Course;

public interface DataService {

	Course getXmlCourse();

	Course getXmlCourse2();

	Course getJsonCourse();

	Response getCorona19Status(LocalDate date);

}
