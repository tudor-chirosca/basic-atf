package com.vocalink.crossproduct.infrastructure.config;

import java.util.Objects;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile({"dev", "preprod"})
@RequiredArgsConstructor
public class JNDIDatasourceConfig {

  private final Environment env;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
    return builder.dataSource(dataSource)
        .persistenceUnit("ISS")
        .packages("com.vocalink.crossproduct.infrastructure.jpa")
        .build();
  }

  @Bean
  public DataSource dataSource() throws NamingException {
    String jdbcUrl = env.getProperty("jdbc.url");
    Objects.requireNonNull(jdbcUrl, "Datasource JDBC URL can not be null");
    return (DataSource) new JndiTemplate().lookup(jdbcUrl);
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }
}
