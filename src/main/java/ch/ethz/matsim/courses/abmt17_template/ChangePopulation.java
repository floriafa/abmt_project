package ch.ethz.matsim.courses.abmt17_template;

import java.util.Random;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.population.io.PopulationWriter;

public class ChangePopulation {
	Random rnd = new Random();
	Random rndOwn = new Random();
	Random rndAV = new Random();
	int selLen = 100000;
	private double origCO = 50; // We assume an original car ownership of 50

	public Population ChangePop ( Scenario scenario, double carOwnership ) {
		Population population = scenario.getPopulation();
		// Population populationChange = scenario.getPopulation();
		// carOwnership = this.carOwnership;
		// We now change according to the car ownership a certain percentage of car owners to non-car owners

		for(Person person : population.getPersons().values()) {
			if (person.getAttributes().getAttribute("carAvail")=="never")
			{person.getAttributes().putAttribute("carOwn", false);}
			else if (person.getAttributes().getAttribute("carAvail")=="always")
			{person.getAttributes().putAttribute("carOwn", "true");}
			else 
			{ // sometimes available
				int selOwn = rndOwn.nextInt(selLen);
				double decOwn = selOwn/selLen;
				if (decOwn < 0.5) 	{person.getAttributes().putAttribute("carOwn", "true");}
				else 			{person.getAttributes().putAttribute("carOwn", "false");}}

			// Now for those who still own a car, we alter their car ownership.
			if (person.getAttributes().getAttribute("carOwn")=="true")
			{
				double carToAV = origCO  - carOwnership; // here 50 is the assumed original condition
				int selAV = rndAV.nextInt(selLen);
				double decAV = selAV/selLen;
				if (decAV < carToAV/origCO) {
					person.getAttributes().putAttribute("carOwn","true");
				}
			}

			// now based on their ownership, we set the mode choice

			Plan plan = person.getSelectedPlan();
			for( PlanElement pe : plan.getPlanElements()) {
				if( pe instanceof Activity) {
					Activity activity = (Activity) pe;
				} else {
					Leg leg = (Leg) pe;
					if (person.getAttributes().getAttribute("carOwn")== "true" &
							leg.getMode() == "car") { 
						// we do nothing
						// Assume those owning car will remain unchanged

						// here we could do analysis how many trips there are etc.
					} else if (person.getAttributes().getAttribute("carOwn")== "false" &
							leg.getMode() == "car") { 
						int sel = rnd.nextInt(selLen);
						double dec = sel/selLen;
						if (dec < 0.63) { leg.setMode("av");}
						else if (dec <0.98) {leg.setMode("pt");}
						else {leg.setMode("bike");}

						// Assign rand() to each person and set Mode according to the rand value
						// rand values are 0-0.63 AV, 0.63-0.98 PT, 0.98-1.00 Bike
						// Should all these values only be applied by Trip purpose?
						// Our survey was only for work-/educ.-related trips.
					} else if (person.getAttributes().getAttribute("carOwn")=="false" & 
							leg.getMode() == "pt") {
						int sel = rnd.nextInt(selLen);
						double dec = sel/selLen;
						if (dec < 0.63) { leg.setMode("av");}
						else if (dec < 0.98) {leg.setMode("pt");}
						else {leg.setMode("bike");}
						// Assume those owning car will remain unchanged
						// Assign rand() to each person and set Mode according to the rand value
						// rand values are 0-0.63 AV, 0.63-0.98 PT, 0.98-1.00 Bike
						// Should all these values only be applied by Trip purpose?
						// Our survey was only for work-/educ.-related trips.
					}

				}
				// Do we compile the activities and legs again into pe?
			} // % For plan

		} // % For person

		PopulationWriter pw = new PopulationWriter (population);
		pw.write("/home/floriafa/ABMT_project/output/pop"+carOwnership+"car.txt");
		return population;
	}	// % Method

} // % Class
