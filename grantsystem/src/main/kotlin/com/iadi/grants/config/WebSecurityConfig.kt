package com.iadi.grants.config



import com.iadi.grants.api.controller.ReviewerController
import com.iadi.grants.api.controller.StudentController
import com.iadi.grants.services.services.*


import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.config.annotation.CorsRegistry


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!test")
class WebSecurityConfig(var details: MyUserDetailsService,
                        val users:UserService,val students: StudentController,val reviewers: ReviewerController,
                        val sec:SecurityService) : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity){
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .addFilterBefore(UserPasswordAuthenticationFilterToJWT ("/login", super.authenticationManagerBean(),sec,users),
                        UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(UserPasswordSignUpFilterToJWT ("/signup", users,sec,students,reviewers),
                        UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(AnonFilterToJWT("/anon"),
                        UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.headers().frameOptions().disable()  //allow hibernate viewer to display information on browser
        http.headers().frameOptions().sameOrigin() //allow hibernate viewer to display information on browser

        var config = CorsConfiguration();
        config.addExposedHeader("Authorization")
        config.addExposedHeader("Roles")

        http.cors().configurationSource {
            config.applyPermitDefaultValues()
        }

    }

    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.inMemoryAuthentication()
                .withUser("student")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("STUDENT")
                .and()
                .withUser("reviewer")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("REVIEWER")
                .and()
                .withUser("panel_chairman")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("CHAIRMAN", "REVIEWER")
                .and()
                .withUser("admin")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("ADMIN")
                .and()
                .withUser("sponsor")
                .password(BCryptPasswordEncoder().encode("pw"))
                .roles("SPONSOR")
                .authorities(emptyList())
                .and()
                .passwordEncoder(BCryptPasswordEncoder())
                .and()
                .userDetailsService(details)
                .passwordEncoder(BCryptPasswordEncoder())
    }}


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("test")
class WebSecurityConfigTesting(var details: MyUserDetailsService,
                        val users:UserService,
                        val sec:SecurityService) : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity){
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST,"/signup").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.headers().frameOptions().disable()  //allow hibernate viewer to display information on browser
        http.headers().frameOptions().sameOrigin() //allow hibernate viewer to display information on browser

        http.cors().configurationSource {
            CorsConfiguration().applyPermitDefaultValues()
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.inMemoryAuthentication()
                .withUser("student")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("STUDENT")
                .and()
                .withUser("reviewer")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("REVIEWER")
                .and()
                .withUser("panel_chairman")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("CHAIRMAN", "REVIEWER")
                .and()
                .withUser("admin")
                .password(BCryptPasswordEncoder().encode("pw"))
                .authorities(emptyList())
                .roles("ADMIN")
                .and()
                .withUser("sponsor")
                .password(BCryptPasswordEncoder().encode("pw"))
                .roles("SPONSOR")
                .authorities(emptyList())
                .and()
                .passwordEncoder(BCryptPasswordEncoder())
                .and()
                .userDetailsService(details)
                .passwordEncoder(BCryptPasswordEncoder())
    }}