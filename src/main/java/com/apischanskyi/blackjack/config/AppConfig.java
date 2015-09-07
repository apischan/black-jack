package com.apischanskyi.blackjack.config;

import com.apischanskyi.blackjack.Constants;
import com.apischanskyi.blackjack.entity.Round;
import com.apischanskyi.blackjack.entity.CardLog;
import com.apischanskyi.blackjack.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Profile("prod")
@Configuration
@ComponentScan("com.apischanskyi.spring.blackjack")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@Import(CustomBeansConfig.class)
public class AppConfig {

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
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty(Constants.HibernateConstants.HIBERNATE_DRIVER_PROP_NAME));
        dataSource.setUrl(env.getProperty(Constants.HibernateConstants.HIBERNATE_DB_URL_PROP_NAME));
        dataSource.setUsername(env.getProperty(Constants.HibernateConstants.HIBERNATE_DB_USERNAME_PROP_NAME));
        dataSource.setPassword(env.getProperty(Constants.HibernateConstants.HIBERNATE_DB_PASSWORD_PROP_NAME));
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty(Constants.HibernateConstants.HIBERNATE_DIALECT,
                env.getProperty(Constants.HibernateConstants.HIBERNATE_DIALECT_PROP_NAME));
        properties.setProperty(Constants.HibernateConstants.HIBERNATE_SHOW_SQL,
                env.getProperty(Constants.HibernateConstants.HIBERNATE_SHOW_SQL_PROP_NAME));
//        properties.setProperty(HibernateConstants.HIBERNATE_HBM2DDL_AUTO_PROP_NAME,
//                env.getProperty(HibernateConstants.HIBERNATE_HBM2DDL_AUTO_PROP_NAME));
        return properties;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager() throws IOException {
        return new HibernateTransactionManager(sessionFactory());
    }

}
