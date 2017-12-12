import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.io.NetworkReaderMatsimV2;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;

public class exercise4 {

	public static void main(String[] args) {
		Config config = ConfigUtils.createConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		
		PopulationReader pr = new PopulationReader(scenario);
		
		pr.readFile(args[0]);
		
		NetworkReaderMatsimV2 nr = new NetworkReaderMatsimV2(scenario.getNetwork());
		
		nr.readFile(args[1]);
		
		EventsManager em = EventsUtils.createEventsManager();
		
		em.addHandler(null); // eigene Handlers einfügen
		
		MatsimEventsReader eventsReader = new MatsimEventsReader(em);
		
		eventsReader.readFile(args[2]);
		
	}
}
