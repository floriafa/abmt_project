package ch.ethz.matsim.courses.abmt17_template;


import org.matsim.core.controler.AbstractModule;
import org.matsim.core.scoring.ScoringFunctionFactory;

import abmt17.scoring.ABMTScoringFunctionFactory;

public class AVScoringModuleForABMT extends AbstractModule {
	@Override
	public void install() {
		bind(ABMTScoringFunctionFactory.class).asEagerSingleton();
		bind(ScoringFunctionFactory.class).to(AVScoringFunctionFactoryForABMT.class).asEagerSingleton();
	}
}
