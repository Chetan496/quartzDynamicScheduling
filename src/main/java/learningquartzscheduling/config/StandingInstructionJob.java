package learningquartzscheduling.config;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Configuration
public class StandingInstructionJob extends QuartzJobBean {
	static final Logger log = LoggerFactory.getLogger(StandingInstructionJob.class);

	private String jobName;



	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
	
		log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        log.info("Will start executing job with data: {} ", jobDataMap.getWrappedMap());
	     
	}
}
