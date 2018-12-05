package mobivik.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
        		.antMatchers("/","/fromserver","/test").permitAll()
				.antMatchers("/css/**","/images/**","/js/**","/plugins/**").permitAll()
        		.antMatchers("/admin").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
					.formLogin()
						.loginPage("/login")
						.failureUrl("/login-error")
						.permitAll()
				.and()
					.logout()
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/")
						.permitAll()
                .and()
					.exceptionHandling()
						.accessDeniedPage("/403");
	}




}