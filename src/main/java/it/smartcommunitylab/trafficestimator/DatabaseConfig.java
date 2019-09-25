package it.smartcommunitylab.trafficestimator;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.DefaultJpaDialect;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean(name = "trafficNewDataSource")
    public DriverManagerDataSource getTrafficNewDataSource() {
        DriverManagerDataSource bean = new DriverManagerDataSource();

        bean.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        bean.setUrl(env.getProperty("db.trafficnew.url"));
        bean.setUsername(env.getProperty("db.trafficnew.username"));
        bean.setPassword(env.getProperty("db.trafficnew.password"));

        return bean;
    }

    @Bean(name = "trafficNewEntityManagerFactory")
    public EntityManagerFactory getTrafficNewEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
//        bean.setPersistenceUnitName(env.getProperty("jdbc.name"));
        bean.setDataSource(getTrafficNewDataSource());

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform(env.getProperty("spring.jpa.database-platform"));
        bean.setJpaVendorAdapter(adapter);

        bean.setJpaDialect(new DefaultJpaDialect());

        Properties props = new Properties();
        bean.setJpaProperties(props);
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    @Bean(name = "trafficNewEntityManager")
    public EntityManager trafficNewEntityManager() {
        return getTrafficNewEntityManagerFactory().createEntityManager();
    }

    @Bean(name = "trafficDataSource")
    public DriverManagerDataSource getTrafficDataSource() {
        DriverManagerDataSource bean = new DriverManagerDataSource();

        bean.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        bean.setUrl(env.getProperty("db.traffic.url"));
        bean.setUsername(env.getProperty("db.traffic.username"));
        bean.setPassword(env.getProperty("db.traffic.password"));

        return bean;
    }

    @Bean(name = "trafficEntityManagerFactory")
    public EntityManagerFactory getTrafficEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
//        bean.setPersistenceUnitName(env.getProperty("jdbc.name"));
        bean.setDataSource(getTrafficDataSource());

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform(env.getProperty("spring.jpa.database-platform"));
        bean.setJpaVendorAdapter(adapter);

        bean.setJpaDialect(new DefaultJpaDialect());

        Properties props = new Properties();
        bean.setJpaProperties(props);
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    @Bean(name = "trafficEntityManager")
    public EntityManager trafficEntityManager() {
        return getTrafficEntityManagerFactory().createEntityManager();
    }

}
