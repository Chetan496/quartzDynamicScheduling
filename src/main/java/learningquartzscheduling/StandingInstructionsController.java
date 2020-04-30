package learningquartzscheduling;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StandingInstructionsController {
	
	@Autowired
	ServiceForSchedulingStandingInstructions serviceForSchedulingStandingInstructions ;
	
	@PostMapping("/goal")
	public ResponseEntity<?> createStandingInstruction(@RequestBody final StandingInstructionSchedule standingInstructionSchedule) throws SchedulerException {
		
		serviceForSchedulingStandingInstructions.createStandingInstruction(standingInstructionSchedule);
		return ResponseEntity.status(HttpStatus.CREATED).build() ;
		
	}
	
	
	@GetMapping("/{clientId}/goals")
	public ResponseEntity<List<StandingInstructionSchedule>> getStandingInstructionsScheduled(@PathVariable("clientId") final String clientId) throws SchedulerException {
		
		List<StandingInstructionSchedule> allStandingInstructionsForGivenClient = serviceForSchedulingStandingInstructions.getAllStandingInstructionsForGivenClient(clientId) ;
		return ResponseEntity.ok(allStandingInstructionsForGivenClient);
	}
	
}
