package com.example.thuedientu;
import org.apache.poi.util.IOUtils;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThuedientuApplication {

	public static void main(String[] args) {

		IOUtils.setByteArrayMaxOverride(100_000_000);

		SpringApplication.run(ThuedientuApplication.class, args);
	}

}
