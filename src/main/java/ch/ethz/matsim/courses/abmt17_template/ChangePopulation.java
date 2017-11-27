package ch.ethz.matsim.courses.abmt17_template;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.population.io.PopulationWriter;

public class ChangePopulation {
public void ChangePop ( Scenario scenario ) {
	Population population = scenario.getPopulation();
	for(Person person : population.getPersons().values()) {
		Plan plan = person.getSelectedPlan();
		for( PlanElement pe : plan.getPlanElements()) {
			if( pe instanceof Activity) {
				Activity activity = (Activity) pe;
			} else {
				Leg leg = (Leg) pe;
				
			}
		}
	}
	PopulationWriter pw = new PopulationWriter (population);
	pw.write("/home/floriafa/output/pop.txt");
	
}	
}	

