package technikal.task.fishmarket.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import technikal.task.fishmarket.services.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;
    private final EncoderConfig encoderConfig;

    public SecurityConfig(CustomUserDetailService userDetailsService, EncoderConfig encoderConfig) {
        this.userDetailsService = userDetailsService;
        this.encoderConfig = encoderConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll() // Доступ к публичным страницам
                        .requestMatchers("/fish/create").hasRole("ADMIN")
                        .requestMatchers("/fish").hasRole("USER")
                        .anyRequest().authenticated() // Остальные страницы требуют входа
                )
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF (на тестах)
                .formLogin(login -> login
                        .defaultSuccessUrl("/fish/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoderConfig.passwordEncoder());
        return provider;
    }
}
