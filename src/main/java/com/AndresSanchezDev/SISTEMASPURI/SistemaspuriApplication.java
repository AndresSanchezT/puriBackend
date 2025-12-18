package com.AndresSanchezDev.SISTEMASPURI;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:4200")
public class SistemaspuriApplication {

	public static void main(String[] args) throws FileNotFoundException, JRException {

		SpringApplication.run(SistemaspuriApplication.class, args);

		InputStream jrxmlStream = new FileInputStream(
				"src/main/resources/reportes/puri_boleta.jrxml"
		);

		JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

		JRSaver.saveObject(jasperReport,
				"src/main/resources/reportes/puri_boleta.jasper"
		);

		System.out.println("✅ Reporte compilado: puri_boleta.jasper");
	}

}
