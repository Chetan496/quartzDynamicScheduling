package learningquartzscheduling.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfiguration {
	
	private static Logger	logger	= LoggerFactory.getLogger(QuartzConfiguration.class);
	@Autowired
	private DataSource		datasource;

	@Bean(name = "DynamicSchedulerFactoryBean")
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException
	{
		logger.info("dynamicSchedulerFactoryBean() START");
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setBeanName("DynamicScheduler");
		schedulerFactoryBean.setAutoStartup(true);
		schedulerFactoryBean.setConfigLocation(loadQuartzProps());
		schedulerFactoryBean.setDataSource(datasource);
		return schedulerFactoryBean;
	}
	
	private Resource loadQuartzProps()
	{
		return new ClassPathResource("quartz.properties");
	}
}
