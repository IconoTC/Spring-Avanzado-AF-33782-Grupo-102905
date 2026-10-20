package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
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
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.aop.AuthenticationService;
import com.example.aop.introductions.Visible;
import com.example.aop.introductions.VisibleAspect;
import com.example.base.DummyAsync;
import com.example.base.DummyJSpecify;
import com.example.base.DummyRetry;
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
import com.example.ioc.notificaciones.TwitterSender;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableResilientMethods
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
//			try {
//				if(dummy.getCadena() != null)
//				System.out.println(dummy.getCadena().toUpperCase());
//					System.out.println(dummy.getCadenaSegura().orElse("").toUpperCase());
//				dummy.setCadenaSegura(null);				
//			} catch (Exception ex) {
//				System.err.println("Tratamiento de la excepcion");
//				ex.printStackTrace();
//			}
//		};
//	}
	
	@Autowired
	NotificationService notify;
	
//	@Bean
	CommandLineRunner ioc(ServicioCadenas srv, String otroAutor, @Value("${spring.application.name}") String nombre, Rango rango, 
			AuthenticationService auth) {
		return arg -> {
			try {
	//			var nombre = "mundo";
				notify.add("Hola %s".formatted(nombre));
				notify.add("Hola %s".formatted(otroAutor));
				notify.add("que tal");
				
				//ServicioCadenas srv = new ServicioCadenasImpl(new RepositorioCadenasImpl(new ConfiguracionImpl(notify), notify), notify);
				srv.get().forEach(notify::add);
				auth.login();
				srv.add("algo");
				notify.getListado().forEach(System.out::println);
				System.err.println(rango);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
	
//	@Bean
	CommandLineRunner introduccionesAOP(@Tweet Sender sender) {
		return arg -> {
			System.err.println(sender.getClass().getCanonicalName());
			sender.send("Hola mundo");
			if(sender instanceof Visible v) {
				IO.println(v.isVisible() ? "ES visibe" : "NO es visible");
				v.mostrar();
				IO.println(v.isVisible() ? "ES visibe" : "NO es visible");
				v.ocultar();
				IO.println(v.isVisible() ? "ES visibe" : "NO es visible");
			} else {
				System.err.println("NO implementa el interfaz Visible.");
			}
			
		};
	}
	
//	@Bean
	CommandLineRunner generarProxyManual() {
		return arg -> {
			var target = new TwitterSender();
			AspectJProxyFactory factory = new AspectJProxyFactory(target);
			factory.addAspect(VisibleAspect.class);

			var sender = factory.getProxy();

			System.err.println(sender.getClass().getCanonicalName());
			target.send("Hola mundo");
			if(sender instanceof Visible v) {
				IO.println(v.isVisible() ? "ES visibe" : "NO es visible");
				v.mostrar();
				IO.println(v.isVisible() ? "ES visibe" : "NO es visible");
				v.ocultar();
				IO.println(v.isVisible() ? "ES visibe" : "NO es visible");
			} else {
				System.err.println("NO implementa el interfaz Visible.");
			}
			
		};
	}

	@Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5, initialDelay = 5)
	void mostrarNotificaciones() {
//		 IO.println("Han pasado 5 segundos");
		if (notify.hasMessages()) {
			IO.println("==============>");
			notify.getListado().forEach(IO::println);
			notify.clear();
			IO.println("<==============");
		}
	}

//	@Bean
	CommandLineRunner asincrono(DummyAsync dummy) {
		return arg -> {
			var obj = dummy; // new DummyAsync();
			System.err.println(obj.getClass().getCanonicalName());
//			obj.ejecutarAutoInvocado(1);
//			obj.ejecutarAutoInvocado(2);
			obj.ejecutarTareaSimpleAsync(1);
			obj.ejecutarTareaSimpleAsync(2);
			obj.calcularResultadoAsync(10, 20, 30, 40, 50).thenAccept(result -> notify.add(result));
			obj.calcularResultadoAsync(1, 2, 3).thenAccept(result -> notify.add(result));
			obj.calcularResultadoAsync().thenAccept(result -> notify.add(result));
			System.err.println("Termino de mandar hacer las cosas");
		};
	}

//	@Bean
	CommandLineRunner resiliencia(DummyRetry dummy) {
		return arg -> {
			IO.println("ReintentaConAnotacion (3)");
			try {
				IO.println("------------------> reintentaConAnotacion: " + dummy.reintentaConAnotacion(3));
			} catch (Exception e) {
				System.err.println("ERROR: REINTENTOS AGOTADOS reintentaConAnotacion 3: " + e.getMessage());
			}
			dummy.reinicia();
			IO.println("ReintentaConAnotacion (5)");
			try {
				IO.println("------------------> reintentaConAnotacion: " + dummy.reintentaConAnotacion(5));
			} catch (Exception e) {
				System.err.println("ERROR: REINTENTOS AGOTADOS reintentaConAnotacion 5: " + e.getMessage());
			}
			dummy.reinicia();
			IO.println("ReintentaConTemplate (3)");
			try {
				IO.println("------------------> reintentaConTemplate: " + dummy.reintentaConTemplate(3));
			} catch (Exception e) {
				System.err.println("ERROR: REINTENTOS AGOTADOS reintentaConTemplate: " + e.getMessage());
			}
			dummy.reinicia();
			IO.println("ReintentaConTemplate (5)");
			try {
				IO.println("------------------> reintentaConTemplate: " + dummy.reintentaConTemplate(5));
			} catch (Exception e) {
				System.err.println("ERROR: REINTENTOS AGOTADOS reintentaConTemplate: " + e.getMessage());
			}
		};
	}

	@Bean
	CommandLineRunner limites(DummyRetry dummy, DummyAsync obj) {
		return arg -> {
			for (var i = 1; ++i <= 10;) {
				obj.calcularResultadoAsync(10 * i, 20 * i, 30 * i, 40 * i, 50 * i).exceptionally(e -> {
					return "ERROR %s".formatted(e.getMessage());
				}).thenAccept(result -> notify.add(result));
			}
		};
	}

}
