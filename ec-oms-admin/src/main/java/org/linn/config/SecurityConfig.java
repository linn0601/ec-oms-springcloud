package org.linn.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * security配置，以便微服务可以注册
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final String adminContextPath;

	public SecurityConfig(AdminServerProperties adminServerProperties) {
		adminContextPath = adminServerProperties.getContextPath();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(adminContextPath + "/");

		http.authorizeRequests()
			.antMatchers(adminContextPath + "/assets/**").permitAll()
			.antMatchers(adminContextPath + "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage(adminContextPath + "/login")
			.successHandler(successHandler)
			.and()
			.logout().logoutUrl(adminContextPath + "/logout")
			.and()
			.httpBasic()
			.and()
			.csrf()
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			// 忽略部分csrf
			.ignoringAntMatchers(
				adminContextPath + "/instances",
				adminContextPath + "/actuator/**");
	}
}
