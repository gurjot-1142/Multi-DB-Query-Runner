package com.project;

import com.project.MultiDbQueryRunner.service.QueryExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiDbQueryRunnerApplication implements CommandLineRunner {

	@Autowired
	private QueryExecutorService executorService;

	public static void main(String[] args) {
		SpringApplication.run(MultiDbQueryRunnerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		executorService.execute();
		System.exit(0);
	}
}