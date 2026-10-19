package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.base.DummyJSpecify;
import com.example.ioc.NotificationService;
import com.example.ioc.Rango;
import com.example.ioc.anotaciones.EMail;
import com.example.ioc.anotaciones.Tweet;
import com.example.ioc.contratos.ServicioCadenas;
import com.example.ioc.implementaciones.ConfiguracionImpl;
import com.example.ioc.implementaciones.RepositorioCadenasImpl;
import com.example.ioc.implementaciones.ServicioCadenasImpl;
import com.example.ioc.notificaciones.Sender;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		System.err.println("Aplicación arrancada ...");
		log.warn("------------------> Aplicación arrancada ...");
	}
//	@Bean
//	CommandLineRunner arranque() {
//		return arg -> {
//			System.err.println("Aplicación arrancada con bean");
//		};
//	}
//	@Bean
//	CommandLineRunner nullable(DummyJSpecify dummy) {
//		return arg -> {
//			//var dummy = new DummyJSpecify();
//			
//			if(dummy.getCadena() != null)
//			System.out.println(dummy.getCadena().toUpperCase());
//				System.out.println(dummy.getCadenaSegura().orElse("").toUpperCase());
////			dummy.setCadenaSegura(null);
//		};
//	}
	
	@Autowired
	NotificationService notify;
	
	@Bean
	CommandLineRunner ioc(ServicioCadenas srv, String otroAutor, @Value("${spring.application.name}") String nombre, Rango rango) {
		return arg -> {
//			var nombre = "mundo";
			notify.add("Hola %s".formatted(nombre));
			notify.add("Hola %s".formatted(otroAutor));
			notify.add("que tal");
			
			//ServicioCadenas srv = new ServicioCadenasImpl(new RepositorioCadenasImpl(new ConfiguracionImpl(notify), notify), notify);
			srv.get().forEach(notify::add);
			notify.getListado().forEach(System.out::println);
			System.err.println(rango);
		};
	}
	
//	@Bean
//	CommandLineRunner porNombre(@Tweet Sender sender, @EMail Sender correo) {
//		return arg -> {
//			sender.send("Hola mundo");
//			correo.send("Hola cartero");
//		};
//	}
//
//	
//	@Bean
//	CommandLineRunner todos(List<Sender> senders) {
//		return arg -> {
//			senders.forEach(s -> s.send("envio con " + s.getClass().getCanonicalName()));
//		};
//	}

}
