package com.tomacat.blog.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.tomacat.blog")  // Добавьте явное указание пакета
@PropertySource("classpath:application.properties")
public class WebConfiguration {}