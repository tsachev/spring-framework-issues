package org.springframework.issues;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class DelegatingWebMvcConfigurationWithContentNegotiationManagerTests {

    @Autowired
    ApplicationContext context;

    @Configuration
    @EnableWebMvc
    static class Config {

        @Bean
        TestWebMvcConfigurer testWebMvcConfigurerWithContentNegotiationManager() {
            return new TestWebMvcConfigurer() {
                @Autowired @Lazy
                ContentNegotiationManager contentNegotiationManager;

                /**
                 * Note - it is plausable that users would need/want to autowire in the
                 * ContentNegotiationManager into their interceptors or other MVC plumbing
                 * that is configurable using the WebMvcConfigurer interface.  When the
                 * ContentNegotiationManager is autowired, the "configureContentNegotiation"
                 * callback method never gets called.  See the error message for specific details.
                 */
            };
        }
    }

    @Test
    public void testThatAllMethodsOnTestWebMvcConfigurerGotCalled() {
        TestWebMvcConfigurer testWebMvcConfigurer = context.getBean(TestWebMvcConfigurer.class);
        Assert.assertNotNull(testWebMvcConfigurer);
        Assert.assertEquals(testWebMvcConfigurer.getCallbackMethodsThatWereNotCalled(), TestWebMvcConfigurer.CALLBACK_METHOD_COUNT, testWebMvcConfigurer.getCounterValue());
    }

}

