package at.fhhgb.mc.wasserapp.datarepository;

import java.util.ArrayList;

import at.fhhgb.mc.wasserapp.model.Measuringpoint;
import at.fhhgb.mc.wasserapp.model.River;
import at.fhhgb.mc.wasserapp.model.WaterLevel;

/**
 * Riverdata repository
 * @author mariobaumgartner
 *
 */
public class Riverdatarepository {
	
	public ArrayList<River> getAllRivers(){
		return null;
	}
	public ArrayList<Measuringpoint> getAllMeasuringpoints(River _river){
		return null;
	}
	public ArrayList<WaterLevel> getAllWaterLevels(Measuringpoint _measpoint){
		return null;
	}
	public WaterLevel getLatestWaterLevel(Measuringpoint _measpoint){
		return null;
	}
}
