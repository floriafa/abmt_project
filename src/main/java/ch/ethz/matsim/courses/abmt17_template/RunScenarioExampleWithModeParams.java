package ch.ethz.matsim.courses.abmt17_template;

import java.util.Collection;
import java.util.stream.Collectors;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dynagent.run.DynQSimModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.population.io.PopulationWriter;
import org.matsim.core.replanning.modules.SubtourModeChoice;
import org.matsim.core.scenario.ScenarioUtils;

import abmt17.pt.ABMTPTModule;
import abmt17.scoring.ABMTScoringModule;
import ch.ethz.matsim.av.framework.AVConfigGroup;
import ch.ethz.matsim.av.framework.AVModule;
import ch.ethz.matsim.av.framework.AVQSimProvider;
import ch.ethz.matsim.av.routing.AVRoute;
import ch.ethz.matsim.av.routing.AVRouteFactory;
import ch.ethz.matsim.baseline_scenario.analysis.simulation.ModeShareListenerModule;

/**
 * This is the template for the 2017 ABMT course at ETHZ
 * 
 * With this example you can run the scenario. The only argument to the script
 * is the path to the config file (abmt_config.xml).
 * 
 * In Eclipse, for instance, you can right click on the "main" method and choose
 * "Run As ..." -> "Java Application". At first you will get an error, because
 * no command line argument has been provided. In the run menu "Green Arrow"
 * under the menu bar you can now click on "Run configurations ..." where you
 * can choose "RunScenarioExample". Click on "Arguments" and set the command
 * line arguments to "abmt_config.xml".
 * 
 * Furthermore, add the following to VM aruments: -Xmx10G It will tell Java to
 * use up to 10GB of RAM for the simulation.
 */
public class RunScenarioExampleWithModeParams {
	static public void main(String[] args) {
		// Load the config file (command line argument)
		//		String polyboxDirectory = "/home/floriafa/ABMT_project/";
		String polyboxDirectory = "C:/Users/ADMIN/Documents/AAA_Documents/ABMT_project/";

		int avFleet; // Starts at 100 and is multiplied by two every cycle until 6400
		double carOwnership = 100; // Should be 100, 75, 50, 25, 0. Represents share of current ownership
		int iter = 100;
		String modeChoice = "ChoiceOn";


		while(carOwnership > -1) {

			avFleet = 100;
			while(avFleet < 6401) {
				Config config = ConfigUtils.loadConfig(polyboxDirectory +"scenario/abmt_config" + modeChoice + avFleet + ".xml", new DvrpConfigGroup(), new AVConfigGroup());
				config.controler().setLastIteration(iter);

				config.controler().setOutputDirectory(polyboxDirectory + "output"+ "/" + modeChoice +  "/AV" + avFleet + "/Own" + carOwnership + "/" );
				config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );

				config.controler().setWriteEventsInterval(1);


				Scenario scenario = ScenarioUtils.loadScenario(config);
				// Load scenario
				Controler controler = new Controler(scenario); // Set up simulation controller

				//Change population here

				ChangePopulationModeParams.ChangePop(scenario, carOwnership);

				// We create subPopulations		

				Population population = scenario.getPopulation();


//				Population carless = scenario.getPopulation();
//				Population traditional = scenario.getPopulation();
				//				for(Person person : scenario.getPopulation().getPersons().values())
				//				{
				//					Id<Person> personId = person.getId();
				//					if(person.getAttributes().getAttribute("carOwn")=="true"){
				//						carless.getPersons().remove(person);
				//					} else if (person.getAttributes().getAttribute("carOwn")=="false") {
				//						traditional.getPersons().remove(person);
				//					}
				//				}
				//				PopulationWriter pw1 = new PopulationWriter (carless);
				//				PopulationWriter pw2 = new PopulationWriter (traditional);
				//				pw1.write(polyboxDirectory + "output/carlesspopulation.txt");
				//				pw2.write(polyboxDirectory + "output/traditionalpopulation.txt");

				Population carless = PopulationUtils.createPopulation(config);
				Population traditional = PopulationUtils.createPopulation(config);
				for(Person person : population.getPersons().values()) {
					if (person.getAttributes().getAttribute("carOwn")=="true")
					{
						traditional.getFactory().createPerson(person.getId());
						traditional.getPersons().get(person.getId()).setSelectedPlan(person.getSelectedPlan());
						traditional.getPersons().get(person.getId()).getCustomAttributes().put("age", person.getAttributes().getAttribute("age"));
						traditional.getPersons().get(person.getId()).getCustomAttributes().put("carAvail", person.getAttributes().getAttribute("carAvail"));
						traditional.getPersons().get(person.getId()).getCustomAttributes().put("carOwn", person.getAttributes().getAttribute("carOwn"));
						traditional.getPersons().get(person.getId()).getCustomAttributes().put("employed", person.getAttributes().getAttribute("employed"));
						traditional.getPersons().get(person.getId()).getCustomAttributes().put("hasLicense", person.getAttributes().getAttribute("hasLicense"));
						traditional.getPersons().get(person.getId()).getCustomAttributes().put("sex", person.getAttributes().getAttribute("sex"));
					}
					else if (person.getAttributes().getAttribute("carOwn")=="false")
					{
						carless.getFactory().createPerson(person.getId());
						carless.getPersons().get(person.getId()).setSelectedPlan(person.getSelectedPlan());
						carless.getPersons().get(person.getId()).getCustomAttributes().put("age", person.getAttributes().getAttribute("age"));
						carless.getPersons().get(person.getId()).getCustomAttributes().put("carAvail", person.getAttributes().getAttribute("carAvail"));
						carless.getPersons().get(person.getId()).getCustomAttributes().put("carOwn", person.getAttributes().getAttribute("carOwn"));
						carless.getPersons().get(person.getId()).getCustomAttributes().put("employed", person.getAttributes().getAttribute("employed"));
						carless.getPersons().get(person.getId()).getCustomAttributes().put("hasLicense", person.getAttributes().getAttribute("hasLicense"));
						carless.getPersons().get(person.getId()).getCustomAttributes().put("sex", person.getAttributes().getAttribute("sex"));
					}
				}

								PopulationWriter pw1 = new PopulationWriter (carless);
								PopulationWriter pw2 = new PopulationWriter (traditional);
								pw1.write(polyboxDirectory + "output/carlesspopulation.txt");
								pw2.write(polyboxDirectory + "output/traditionalpopulation.txt");




				//				for(Person person : carless.getPersons().values()) {
				//					if (person.getAttributes().getAttribute("carOwn")=="true")
				//					{carless.removePerson(person.getId());}
				//				}
				//				for(Person person : traditional.getPersons().values()) {
				//					if (person.getAttributes().getAttribute("carOwn")=="false")
				//					{traditional.removePerson(person.getId());}
				//				}

				scenario.getPopulation().getFactory().getRouteFactories().setRouteFactory(AVRoute.class,
						new AVRouteFactory());

				// Some additional modules to create a more realistic simulation
				controler.addOverridingModule(new ABMTScoringModule()); // Required if scoring of activities is used
				controler.addOverridingModule(new ABMTPTModule()); // More realistic "teleportation" of public transport trips
				controler.addOverridingModule(new ModeShareListenerModule()); // Writes correct mode shares in every iteration

				// Additional modules for AVs
				controler.addOverridingModule(new DvrpTravelTimeModule());
				controler.addOverridingModule(new DynQSimModule<>(AVQSimProvider.class));
				controler.addOverridingModule(new AVModule());

				// Fix scoring after AVs have been added to the scenario
				controler.addOverridingModule(new AVScoringModuleForABMT());

				controler.run();

				avFleet = avFleet * 2;
			}
			carOwnership = carOwnership - 25;
		}
	}

}

