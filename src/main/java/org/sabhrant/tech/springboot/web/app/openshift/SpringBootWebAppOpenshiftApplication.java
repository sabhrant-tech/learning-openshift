package org.sabhrant.tech.springboot.web.app.openshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@SpringBootApplication(scanBasePackages="org.sabhrant.tech.springboot.web.app.openshift")
@EnableJpaRepositories("org.sabhrant.tech.springboot.web.app.openshift.repository")
@EntityScan("org.sabhrant.tech.springboot.web.app.openshift.entity")
public class SpringBootWebAppOpenshiftApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebAppOpenshiftApplication.class, args);
	}

}
