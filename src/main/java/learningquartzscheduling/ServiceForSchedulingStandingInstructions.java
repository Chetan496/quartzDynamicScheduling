package learningquartzscheduling;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import learningquartzscheduling.config.StandingInstructionJob;

@Service
public class ServiceForSchedulingStandingInstructions {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceForSchedulingStandingInstructions.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	public void createStandingInstruction(StandingInstructionSchedule standingInstructionSchedule)
			throws SchedulerException {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobDetail jobDetail = buildJobDetail(standingInstructionSchedule);

		// LocalDate startAt = standingInstructionSchedule.getStartFrom() ; /this is
		// what you would use in real case..
		ZonedDateTime startAt = ZonedDateTime.now().plusMinutes(1);
		Trigger jobTrigger = buildJobTrigger(jobDetail, startAt, standingInstructionSchedule.getFrequencyUnit(),
				standingInstructionSchedule.getFrequency());

		scheduler.scheduleJob(jobDetail, jobTrigger);

	}

	public List<StandingInstructionSchedule> getAllStandingInstructionsForGivenClient(final String clientId) throws SchedulerException {
		
		logger.info("input client id is {} ", clientId);
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals("standingInstructionJob") ;
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		
		Stream<JobDetail> filteredJobDetailsStream = jobKeys.parallelStream().map(jobKey -> {
			try {
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				logger.info("jobDetail: {} ", jobDetail.getJobDataMap().getWrappedMap());
				return jobDetail;
			} catch (SchedulerException e) {
				return null;
			}
		}).filter(jobDetail -> {
			
			if(jobDetail == null) {
				return false;
			}
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			logger.info("jobDataMap: {} ", jobDataMap.getWrappedMap());
			final String clientIdToCompareFromStream = jobDataMap.getString("clientId");
			logger.info("is client id matching: ",clientIdToCompareFromStream.equals(clientId) );
			return clientIdToCompareFromStream.equals(clientId);
		});
		
		
		Stream<StandingInstructionSchedule> standingInstrScheduleStream = filteredJobDetailsStream.map(jobDetail -> {
			
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			StandingInstructionSchedule standingInstructionSchedule = new StandingInstructionSchedule();
			standingInstructionSchedule.setClientId(jobDataMap.getString("clientId"));
			standingInstructionSchedule.setSavingsAcctId(jobDataMap.getString("savingsAcctId"));
			standingInstructionSchedule.setGoalAcctId(jobDataMap.getString("goalAcctId"));
			standingInstructionSchedule.setStartFrom((LocalDate) jobDataMap.get("startFrom"));
			standingInstructionSchedule.setFrequencyUnit(jobDataMap.getString("frequencyUnit"));
			standingInstructionSchedule.setFrequency(jobDataMap.getInt("frequency"));
			return standingInstructionSchedule;
		});
		
		
		return standingInstrScheduleStream.collect(Collectors.toList()) ;
	}

	public void modifyStandingInstruction(StandingInstructionSchedule standingInstructionSchedule) {

	}

	public void cancelStandingInstruction(StandingInstructionSchedule standingInstructionSchedule) {

	}

	private JobDetail buildJobDetail(StandingInstructionSchedule standingInstructionSchedule) {
		JobDataMap jobDataMap = new JobDataMap();

		jobDataMap.put("clientId", standingInstructionSchedule.getClientId());
		jobDataMap.put("savingsAcctId", standingInstructionSchedule.getSavingsAcctId());
		jobDataMap.put("goalAcctId", standingInstructionSchedule.getGoalAcctId());
		jobDataMap.put("startFrom", standingInstructionSchedule.getStartFrom());
		jobDataMap.put("frequency", standingInstructionSchedule.getFrequency());
		jobDataMap.put("frequencyUnit", standingInstructionSchedule.getFrequencyUnit());

		final String jobKey = new StringBuilder(standingInstructionSchedule.getClientId())
				.append(standingInstructionSchedule.getSavingsAcctId())
				.append(standingInstructionSchedule.getGoalAcctId())
				.append(standingInstructionSchedule.getStartFrom().format(DateTimeFormatter.BASIC_ISO_DATE)).toString();

		return JobBuilder.newJob(StandingInstructionJob.class).withIdentity(jobKey, "standingInstructionJob")
				.withDescription("Standing Instruction job").usingJobData(jobDataMap).storeDurably().build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt, String frequencyUnit, int frequency) {

		IntervalUnit intervalUnit = getQuartzTimeUnit(frequencyUnit);
		CalendarIntervalScheduleBuilder intervalSchedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
				.withInterval(frequency, intervalUnit).withMisfireHandlingInstructionIgnoreMisfires();

		return TriggerBuilder.newTrigger().forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "standing-instruction-triggers")
				.withDescription("Standing Instruction Trigger").startAt(Date.from(startAt.toInstant()))
				.withSchedule(intervalSchedule).build();
	}

	private IntervalUnit getQuartzTimeUnit(String frequencyUnit) {

		if (frequencyUnit.equals("MINUTE")) {
			return IntervalUnit.MINUTE;
		}
		if (frequencyUnit.equals("HOUR")) {
			return IntervalUnit.HOUR;
		}
		if (frequencyUnit.equals("DAY")) {
			return IntervalUnit.DAY;
		}
		if (frequencyUnit.equals("WEEK")) {
			return IntervalUnit.WEEK;
		}
		if (frequencyUnit.equals("MONTH")) {
			return IntervalUnit.MONTH;
		}
		return IntervalUnit.MINUTE;
	}

}
