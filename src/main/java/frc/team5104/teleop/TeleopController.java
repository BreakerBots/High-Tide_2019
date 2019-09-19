package frc.team5104.teleop;

/** A twix rapper for all the requirements in a teleop controller */ 
public abstract class TeleopController {
	/** Return the name of this teleop controller (for prints) */
	protected abstract String getName();
	/** Called periodically from the robot loop */
	protected abstract void update();
}
