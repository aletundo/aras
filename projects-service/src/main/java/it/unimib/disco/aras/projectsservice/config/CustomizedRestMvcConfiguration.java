package it.unimib.disco.aras.projectsservice.config;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

import it.unimib.disco.aras.projectsservice.entity.Project;
import it.unimib.disco.aras.projectsservice.entity.Version;

@Component
public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(Project.class, Version.class);
  }
}


