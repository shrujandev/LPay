package net.javaguides.sms.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/start","/students","/signup","/bankslist","/hello","/urlone/verifyAccount","/UPI/RegisterAccount").permitAll()
                .requestMatchers("/loggedIn","/").authenticated()
            )
            .formLogin(
                formLogin -> formLogin
                    .loginPage("/login")
                    .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            );
        return http.build();
}

//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    	http.csrf().disable()
//        .authorizeHttpRequests(requests -> requests
//                .requestMatchers("/start","/students","/signup","/greeting","/hello").permitAll()
//                .requestMatchers("/loggedIn","/").authenticated()
//            )
//        
//            .formLogin(
//            		formLogin -> formLogin
//                    .loginPage("/login")
//                    .permitAll()
//            )
//            .logout(logout -> logout
//            		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                    .invalidateHttpSession(true)
//                    .clearAuthentication(true)
//                    .logoutSuccessUrl("/login?logout")
//                    .permitAll()
//                    
//            );
////        http.csrf().disable();
            
//        return http.build();
//    }


   

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
    
    
}
