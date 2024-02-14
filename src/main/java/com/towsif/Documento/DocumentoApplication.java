package com.towsif.Documento;

import com.towsif.Documento.service.FileStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocumentoApplication implements CommandLineRunner
{
	@Resource
	FileStorageService fileStorageService;

	public static void main(String[] args)
	{
		SpringApplication.run(DocumentoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		fileStorageService.init();
	}
}
