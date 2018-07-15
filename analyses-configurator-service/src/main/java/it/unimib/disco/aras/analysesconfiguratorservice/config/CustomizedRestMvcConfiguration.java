/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.config;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

import it.unimib.disco.aras.analysesconfiguratorservice.entity.AnalysisConfiguration;

/**
 * The Class CustomizedRestMvcConfiguration.
 */
@Component
public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

  /* (non-Javadoc)
   * @see org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter#configureRepositoryRestConfiguration(org.springframework.data.rest.core.config.RepositoryRestConfiguration)
   */
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(AnalysisConfiguration.class);
  }
}


