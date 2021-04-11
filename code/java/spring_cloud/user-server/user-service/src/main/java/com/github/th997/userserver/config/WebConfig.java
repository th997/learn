package com.github.th997.userserver.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.th997.userserver.service.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private Logger log = LoggerFactory.getLogger(WebConfig.class);

    //    @Bean
//    public UndertowServletWebServerFactory embeddedServletContainerFactory() {
//        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
//        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
//        return factory;
//    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HandlerMethodArgumentResolver() { // 参数注入
            @Override
            public boolean supportsParameter(MethodParameter methodParameter) {
                return UserBean.class.equals(methodParameter.getParameterType());
            }

            @Override
            public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
                UserBean user = new UserBean();
                user.setName("xxx");
                return user;
            }
        });
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter converter : converters) {
//            if (converter instanceof MappingJackson2HttpMessageConverter) {
//                MappingJackson2HttpMessageConverter converterJson = (MappingJackson2HttpMessageConverter) converter;
//                converterJson.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            }
            log.info("extendMessageConverters: " + converter);
        }
    }
}