package com.lcwd.electronic.config;

import com.lcwd.electronic.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;


   private final String[] PUBLIC_URLS={
         "/swagger-ui/**",
           "/webjars/**",
           "/swagger-resources/**",
           "/v3/api-docs",
          "/v2/api-docs"
    };

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //for password encode
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }


    // ********  JWT authentication   *******
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.
                csrf()
                .disable()

                .authorizeRequests()
                .antMatchers("/auth/login")
                .permitAll()
                .antMatchers("/auth/google")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/users")
                .permitAll()
                .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    //CORS Configuration
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        //configuration.setAllowedOrigins(Arrays.asList("http://domain2.com","http://localhost:4200"));
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));

        filterRegistrationBean.setOrder(-100);
        return filterRegistrationBean;

    }



    // ********  Basic authentication   *******


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.
//                csrf()
//                .disable()
//                .cors()
//                .disable()
//
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//
//        return http.build();
//    }


    // ********  form based Login customization   *******


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("login.html")
//                .loginProcessingUrl("process-url")
//                .defaultSuccessUrl("error")
//                .and()
//                .logout()
//                .logoutUrl("/logout");
//        return http.build();
//    }


    // ********  hard code for user and password   *******

//    @Bean
//    public UserDetailsService userDetailsService(){
//        //users create
//        UserDetails normal = User.builder()
//                .username("Radha")
//                .password(passwordEncoder().encode("radha"))   //it's compulsory in new spring version   so we create a new bean for this ...(down below)
//                .roles("NORMAL")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("Pawan")
//                .password(passwordEncoder().encode("pawan123"))
//                .roles("ADMIN")
//                .build();
//
//// InMemoryUserDetailsManager -is implementation class of UserDetailsService
//        return new InMemoryUserDetailsManager(normal,admin);
//    }
//
//    //for password encode
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//
//        return new BCryptPasswordEncoder();
//    }
}
