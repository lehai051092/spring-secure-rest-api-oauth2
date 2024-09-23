package example.springsecurerestapioauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@SpringBootApplication
public class SpringSecureRestApiOauth2Application {

    @Bean
    SecurityFilterChain appSecurity(HttpSecurity http, AuthenticationEntryPoint entryPoint) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/cashcards/**").hasAuthority("SCOPE_cashcard:read")
                        .requestMatchers("/cashcards/**").hasAnyAuthority("SCOPE_cashcard:write")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .authenticationEntryPoint(entryPoint)
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecureRestApiOauth2Application.class, args);
    }
}
