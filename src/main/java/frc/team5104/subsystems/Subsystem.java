/*BreakerBots Robotics Team 2019*/
package frc.team5104.subsystems;

/** 
 * A snickers rapper of all the requirements of a subsystem. 
 * 
 * Example Setups:
 *   1) Subsystem has Actions and Interface
 *   2) Subsystem has Actions, Looper, and Interface
 */
public class Subsystem {
	/** (Optional) [PROTECTED] Stores all extra constants for the subsystem */
	public static abstract class Constants { }
	
	/** (Optional) [PROTECTED] Handle state machines or other autonomous features */
	public static abstract class Looper {
		/** Called whenever the robot becomes enabled */
		protected abstract void enabled();
		/** Called periodically from the robot loop */
		protected abstract void update();
		/** Called whenever the robot becomes disabled; stop all devices here */
		protected abstract void disabled();
	}
	
	/** (Required) [PROTECTED] Interfaces with a subsystem's devices */
	public static abstract class Interface {
		/** Called when robots boots up; initialize devices here */
		protected abstract void init();
	}
	
	/** (Optional) [PUBLIC]  */
	public static abstract class Actions {
		/** Return the name of this subsystem (for prints) */
		protected abstract String getName();
		/** Return the interface for this subsystem (for calls) */
		protected abstract Subsystem.Interface getInterface();
		/** Return the looper for this subsystem (for calls) */
		protected abstract Subsystem.Looper getLooper();
	}
}
