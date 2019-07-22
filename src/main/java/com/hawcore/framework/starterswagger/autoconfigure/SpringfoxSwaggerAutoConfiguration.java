package com.hawcore.framework.starterswagger.autoconfigure;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.hawcore.framework.starterswagger.config.SwaggerCustomIgnore;
import com.hawcore.framework.starterswagger.config.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.withMethodAnnotation;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SpringfoxSwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties swaggerProperties;

    //组织Docket对象，翻译过来就是摘要的意思，它是生成API文档的核心对象，里面配置一些必要的信息
    @Bean
    @ConditionalOnProperty("swagger.doc.prefixs")
    public Docket swaggerSpringMvcPlugin() {

        //指定规范，这里是SWAGGER_2
        return new Docket(DocumentationType.SWAGGER_2)
                //设定Api文档头信息，这个信息会展示在文档UI的头部位置
                .apiInfo(swaggerDemoApiInfo())
                .select()
                //添加过滤条件，谓词过滤predicate，这里是自定义注解进行过滤
                .apis(not(withMethodAnnotation(SwaggerCustomIgnore.class)))
                //这里配合@ComponentScan一起使用，又再次细化了匹配规则(当然，我们也可以只选择@ComponentScan、paths()方法当中的一中)
                .paths(allowPaths())
                .build();
    }

    /**
     * 自定义API文档基本信息实体
     *
     * @return
     */
    private ApiInfo swaggerDemoApiInfo() {
        //构建联系实体，在UI界面会显示
        Contact contact = new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactWebsite(), swaggerProperties.getContactEmail());
        return new ApiInfoBuilder()
                .contact(contact)
                //文档标题
                .title(swaggerProperties.getTitle())
                //文档描述
                .description(swaggerProperties.getDescription())
                //文档版本
                .version(swaggerProperties.getVersion())
                .build();
    }

    /**
     * path匹配规则
     *
     * @return
     */
    protected Predicate<String> allowPaths() {
        String prefixs = swaggerProperties.getPrefixs();
        if (prefixs != null && prefixs.length() > 0) {
            String[] prefixArr = prefixs.split(",");
            List<Predicate<? super String>> predicates = Lists.newArrayList();
            for (String prifex : prefixArr) {
                predicates.add(regex(prifex + ".*"));
            }
            return or(
                    predicates
            );
        }
        return regex("/no_valid_url_prefix");
    }
}
