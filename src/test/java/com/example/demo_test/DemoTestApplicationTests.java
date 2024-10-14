package com.example.demo_test;

import com.example.demo_test.controllers.StudentController;
import com.example.demo_test.services.StudentService;
import com.example.demo_test.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

	private static Long changingId = 1L;

	@Autowired
	private StudentController studentController;

	@Autowired
	private StudentService studentService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void controllerLoads() {
		assertThat(studentController).isNotNull();
	}

	@Test
	void serviceLoads() {
		assertThat(studentService).isNotNull();
	}

	@Test
	void createTest() throws Exception {
		Student student = new Student(changingId, "Mario", "Rossi", true);
		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult result = mockMvc.perform(post("/student/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		changingId++;
	}

	@Test
	void readAllTest() throws Exception {
		createTest();

		MvcResult result = mockMvc.perform(get("/student/readAll"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> responseStudents = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(responseStudents.size()).isNotZero();
	}

	@Test
	void readTest() throws Exception {
		Long id = changingId;
		createTest();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/student/read/{id}", id))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andReturn();

		Student responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(responseStudent.getId()).isEqualTo(id);
	}

	@Test
	void updateTest() throws Exception {
		Long id = changingId;
		createTest();

		Student updatedStudent = new Student(id, "Michele", "Angeletti", false);
		String studentJSON = objectMapper.writeValueAsString(updatedStudent);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/student/change/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(responseStudent.getId()).isEqualTo(id);
	}

	@Test
	void updateWorkingTest() throws Exception {
		Long id = changingId;
		createTest();

		boolean isWorking = false;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/student/setWorking/{id}",id)
						.param("working",String.valueOf(isWorking)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(responseStudent.getId()).isEqualTo(id);
	}

	@Test
	void deleteTest() throws Exception {
		Long id = changingId;
		createTest();

		mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/{id}",id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}
}