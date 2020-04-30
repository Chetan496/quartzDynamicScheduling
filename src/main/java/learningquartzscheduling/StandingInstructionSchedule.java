package learningquartzscheduling;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import learningquartzscheduling.customserializers.LocalDateDeserializer;
import learningquartzscheduling.customserializers.LocalDateSerializer;

public class StandingInstructionSchedule {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate startFrom;
	
	private String clientId;
	
	private String savingsAcctId;
	
	private String goalAcctId;
	
	private String frequencyUnit; //lets try with hours and minutes nly
	
	private int frequency;  //so we can have schedule like every 2 hours, every two minutes etc
	
	private double goalAmount;
	
	public StandingInstructionSchedule() {
		super();
	}

	public String getFrequencyUnit() {
		return frequencyUnit;
	}

	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}


	
	public LocalDate getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(LocalDate startFrom) {
		this.startFrom = startFrom;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSavingsAcctId() {
		return savingsAcctId;
	}

	public void setSavingsAcctId(String savingsAcctId) {
		this.savingsAcctId = savingsAcctId;
	}

	public String getGoalAcctId() {
		return goalAcctId;
	}

	public void setGoalAcctId(String goalAcctId) {
		this.goalAcctId = goalAcctId;
	}
	
	
	public double getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(double goalAmount) {
		this.goalAmount = goalAmount;
	}

	@Override
	public String toString() {
		return "StandingInstructionSchedule [startFrom=" + startFrom + ", clientId=" + clientId + ", savingsAcctId="
				+ savingsAcctId + ", goalAcctId=" + goalAcctId + ", frequencyUnit=" + frequencyUnit + ", frequency="
				+ frequency + ", goalAmount=" + goalAmount + "]";
	}

	
}
