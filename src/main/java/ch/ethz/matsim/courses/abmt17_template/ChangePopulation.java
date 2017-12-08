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
	public void ChangePop ( Scenario scenario, double carOwnership ) {
		Population population = scenario.getPopulation();
		// carOwnership = this.carOwnership;
		for(Person person : population.getPersons().values()) {
			// Here we decide which part of the population we want to change
			// i.e. as a function of carOwnership
			Plan plan = person.getSelectedPlan();
			for( PlanElement pe : plan.getPlanElements()) {
				if( pe instanceof Activity) {
					Activity activity = (Activity) pe;
				} else {
					Leg leg = (Leg) pe;
					// Assume those owning car will remain unchanged
					// Assign rand() to each person and set Mode according to the rand value
					// rand values are 0-0.63 AV, 0.63-0.98 PT, 0.98-1.00 Bike
					// Should all these values only be applied by Trip purpose?
					// Our survey was only for work-/educ.-related trips.
					leg.setMode(arg0);
				}
				// Do we compile toe activities and legs again into pe ?
				
			}
		}
		PopulationWriter pw = new PopulationWriter (population);
		pw.write("/home/floriafa/output/pop.txt");
	}	
}	

