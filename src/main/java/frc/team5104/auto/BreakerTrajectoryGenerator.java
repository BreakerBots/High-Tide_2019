/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.auto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import frc.team5104.Constants;
import frc.team5104.auto.util.Trajectory;
import frc.team5104.auto.util.TrajectoryGenerator;
import frc.team5104.auto.util.TrajectoryWaypoint;
import frc.team5104.util.console;
import frc.team5104.util.console.c;

/**
 * 
 */
public class BreakerTrajectoryGenerator {
	private static final String cacheDirectory = "/home/lvuser/TrajectoryCache/";
	
	/**
	 * Will either return a cached version of a Trajectory under those points (~500ms)
	 * or will Generate a Trajectory a cache it (~5000ms - ~15000ms)
	 * @param points Waypoints to generate the trajectory from
	 * @return A Trajectory to follow those waypoints
	 */
	public static Trajectory getTrajectory(TrajectoryWaypoint[] points) {
		//Parse trajectory name
		String trajectoryName = "" + AutoConstants.AUTO_MAX_VELOCITY + AutoConstants.AUTO_MAX_ACCEL + AutoConstants.AUTO_MAX_JERK + Constants.AUTO_LOOP_SPEED;
    	for (TrajectoryWaypoint p : points) {
    		trajectoryName += (Double.toString(p.x) + "/" + Double.toString(p.y) + "/" + Double.toString(p.theta));
    	}
    	trajectoryName = "_" + trajectoryName.hashCode();
    	
    	//Read file
    	console.log(c.AUTO, "Looking for Similar Cached Trajectory Under " + trajectoryName);
    	Trajectory trajectory = readFile(trajectoryName);
    	
    	//If the file does not exist, generate a path and save
    	if (trajectory == null) {
    		console.log(c.AUTO, "No Similar Cached Trajectory Found => Generating Path");
    		console.sets.create("MPGEN");
    		trajectory = TrajectoryGenerator.generate(points, 
    				AutoConstants.AUTO_MAX_VELOCITY,
    				AutoConstants.AUTO_MAX_ACCEL,
    				AutoConstants.AUTO_MAX_JERK,
    				1.0 / Constants.AUTO_LOOP_SPEED
    			);
    		writeFile(trajectoryName, trajectory);
    		console.log(c.AUTO, "Trajectory Generation Took " + console.sets.getTime("MPGEN") + "s");
    	}
    	return trajectory;
	}
	
	/**
	 * Finds, reads, and returns the trajectory saved with name
	 */
	private static Trajectory readFile(String name) {
		try {
			File file = new File(cacheDirectory + name);
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Trajectory t = (Trajectory) ois.readObject();
			ois.close();
			fis.close();
			return t;
		}
		catch (Exception e) { return null; }
	}
	
	/** Writes the path to a new file */
	private static void writeFile(String name, Trajectory t) {
		try {
			File directory = new File(cacheDirectory);
			if (!directory.exists()) {
				console.log("missing dir, making");
				directory.mkdir();
			}
			
			FileOutputStream fos = new FileOutputStream(cacheDirectory + name);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(t);
		    oos.close();
		    fos.close();
		}
		catch (Exception e) {
			console.error(e);
		}
	}
}
