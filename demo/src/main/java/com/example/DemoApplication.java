package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.example.base.DummyJSpecify;
import com.example.ioc.GenericoEvent;
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
@EnableAspectJAutoProxy
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
			srv.add("algo");
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

//	@Bean
	CommandLineRunner configuracionEnXML() {
		return _ -> {
			try (var contexto = new FileSystemXmlApplicationContext("applicationContext.xml")) {
				var notify = contexto.getBean(NotificationService.class);
				System.out.println("configuracionEnXML ===================>");
				var srv = (ServicioCadenas) contexto.getBean("servicioCadenas");
				System.out.println(srv.getClass().getName());
				contexto.getBean(NotificationService.class).getListado().forEach(System.out::println);
				System.out.println("===================>");
				srv.get().forEach(notify::add);
				srv.add("Hola mundo");
				notify.add(srv.get(1));
				srv.modify("modificado");
				System.out.println("===================>");
				notify.getListado().forEach(System.out::println);
				notify.clear();
				System.out.println("<===================");
				((Sender) contexto.getBean("sender")).send("Hola mundo");
			}
		};
	}

//	@EventListener
//	private void eventHandler(GenericoEvent ev) {
//		IO.println("Evento generico: %s -> %s".formatted(ev.origen(), ev.carga()));
//	}
//
////	@EventListener
//	private void eventHandler2(GenericoEvent ev) {
//		IO.println("Este es otro tratamiento del evento generico: %s -> %s".formatted(ev.origen(), ev.carga()));
//	}
//
//	@EventListener
//	private void eventStringHandler(String ev) {
//		IO.println("Tramiento Evento cadena: %s".formatted(ev));
//	}

}
