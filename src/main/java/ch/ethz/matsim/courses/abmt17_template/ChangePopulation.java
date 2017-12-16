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

	static Random rnd = new Random();
	static Random rndOwn = new Random();
	static Random rndAV = new Random();
	static private double origCO = 50; // We assume an original car ownership of 50


	public static Population ChangePop ( Scenario scenario, double carOwnership ) { // Should this be population, or is it void?
		Population population = scenario.getPopulation();
		// Population populationChange = scenario.getPopulation();
		// carOwnership = this.carOwnership;
		// We now change according to the car ownership a certain percentage of car owners to non-car owners

		for(Person person : population.getPersons().values()) {
			if (person.getAttributes().getAttribute("carAvail")=="never")
			{person.getAttributes().putAttribute("carOwn", "false");}
			else if (person.getAttributes().getAttribute("carAvail")=="always")
			{person.getAttributes().putAttribute("carOwn", "true");}
			else 
			{ // sometimes available
				double selOwn = rndOwn.nextDouble();
				if (selOwn < 0.5) 	{person.getAttributes().putAttribute("carOwn", "true");}
				else 			{person.getAttributes().putAttribute("carOwn", "false");}}

			// Now for those who still own a car, we alter their car ownership.
			if (person.getAttributes().getAttribute("carOwn")=="true")
			{
				double carToAV = origCO  - carOwnership; // here 50 is the assumed original condition
				double selAV = rndAV.nextDouble();
				if (selAV < carToAV/origCO) {
					person.getAttributes().putAttribute("carOwn","true");
				}
			}
			// Now set all those who do not have a car to have car availability never.
			if (person.getAttributes().getAttribute("carOwn")=="false")
			{person.getAttributes().putAttribute("carAvail", "never");}

			// now based on their ownership, we set the mode choice

			Plan plan = person.getSelectedPlan();
			for( PlanElement pe : plan.getPlanElements()) {
				if( pe instanceof Activity) {
					Activity activity = (Activity) pe;
				} else {
					Leg leg = (Leg) pe;
					leg.setRoute(null);
					if (person.getAttributes().getAttribute("carOwn")== "true" &
							leg.getMode() == "car") { 
						// we do nothing
						// Assume those owning car will remain unchanged

						// here we could do analysis how many trips there are etc.
					} else if (person.getAttributes().getAttribute("carOwn")== "false" &
							leg.getMode() == "car") { 
						double sel = rnd.nextDouble();
						if (sel < 0.63) { leg.setMode("av");}
						else if (sel <0.98) {leg.setMode("pt");}
						else {leg.setMode("bike");}

						// generate rand() and set Mode according to the rand value
						// rand values are 0-0.63 AV, 0.63-0.98 PT, 0.98-1.00 Bike
						// Should all these values only be applied by Trip purpose?
						// Our survey was only for work-/educ.-related trips.
					} else if (person.getAttributes().getAttribute("carOwn")=="false" & 
							leg.getMode() == "pt") {
						double sel = rnd.nextDouble();
						if (sel < 0.63) { leg.setMode("av");}
						else if (sel < 0.98) {leg.setMode("pt");}
						else {leg.setMode("bike");}
						// Assume those owning car will remain unchanged
						// generate rand() and set Mode according to the rand value
						// rand values are 0-0.63 AV, 0.63-0.98 PT, 0.98-1.00 Bike
						// Should all these values only be applied by Trip purpose?
						// Our survey was only for work-/educ.-related trips.
					}

				}
				// Do we compile the activities and legs again into pe?
			} // % For plan

		} // % For person

//		PopulationWriter pw = new PopulationWriter (population);
//		pw.write("/home/floriafa/ABMT_project/output/pop"+carOwnership+"car.txt");
		return population;
	}	// % Method

} // % Class
