package com.apischanskyi.blackjack.config;

import com.apischanskyi.blackjack.Constants.HibernateConstants;
import com.apischanskyi.blackjack.entity.CardLog;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.entity.User;
import org.hibernate.SessionFactory;
import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Profile("dev")
@Configuration
@ComponentScan("com.apischanskyi.blackjack")
@EnableTransactionManagement
@PropertySource("classpath:dev.application.properties")
public class DevAppConfig {

    @Autowired
    private Environment env;

    @Bean
    public HibernateTemplate hibernateTemplate() throws IOException {
        return new HibernateTemplate(sessionFactory());
    }

    @Bean
    public SessionFactory sessionFactory() throws IOException {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource());
        localSessionFactoryBean.setHibernateProperties(hibernateProperties());
        localSessionFactoryBean.setAnnotatedClasses(
                User.class,
                Round.class,
                CardLog.class
        );
        DatabaseManagerSwing.main(new String[]{"--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", ""});
        localSessionFactoryBean.afterPropertiesSet();
        return localSessionFactoryBean.getObject();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setPrettyPrint(true);
        return converter;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:dev/sql/schema.sql")
                .addScript("classpath:dev/sql/test-data.sql")
                .build();
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty(HibernateConstants.HIBERNATE_DIALECT,
                env.getProperty(HibernateConstants.HIBERNATE_DIALECT_PROP_NAME));
        properties.setProperty(HibernateConstants.HIBERNATE_SHOW_SQL,
                env.getProperty(HibernateConstants.HIBERNATE_SHOW_SQL_PROP_NAME));
//        properties.setProperty(HibernateConstants.HIBERNATE_HBM2DDL_AUTO_PROP_NAME,
//                env.getProperty(HibernateConstants.HIBERNATE_HBM2DDL_AUTO_PROP_NAME));
        return properties;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager() throws IOException {
        return new HibernateTransactionManager(sessionFactory());
    }

}
