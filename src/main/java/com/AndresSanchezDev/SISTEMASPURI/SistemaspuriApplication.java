package com.AndresSanchezDev.SISTEMASPURI;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:4200")
public class SistemaspuriApplication {

	public static void main(String[] args) throws FileNotFoundException, JRException {

		SpringApplication.run(SistemaspuriApplication.class, args);

		InputStream jrxmlStream = SistemaspuriApplication.class
				.getClassLoader()
				.getResourceAsStream("reportes/puri_boleta.jrxml");

		if (jrxmlStream == null) {
			throw new RuntimeException("❌ No se encontró puri_boleta.jrxml en classpath");
		}

		JasperReport jasperReport =
				JasperCompileManager.compileReport(jrxmlStream);

		// Opcional: guardar como .jasper en memoria o filesystem temporal
		System.out.println("✅ Reporte JRXML compilado correctamente");
	}

}
