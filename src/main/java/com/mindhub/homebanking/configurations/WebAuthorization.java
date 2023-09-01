package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    //Este metodo se llama una ves que el usuario este finalmente auntenticado lo que significa que segun los permisos/roles que tenga el usuario podra
    //acceder a ciertas urls y si no esta auntenticado significa que debe ser redireccionado a login
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http.authorizeRequests()
                .antMatchers("/auth/**", "/auth/login", "/auth/logout", "/api/clients/current").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/web/**", "/api/**").hasAuthority("CLIENT")
                .anyRequest().denyAll();
        ;

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/auth/login")
                .successForwardUrl("/web/accounts.html");

        http.logout().logoutUrl("/auth/logout");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }



}
