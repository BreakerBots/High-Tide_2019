/*BreakerBots Robotics Team 2019*/
package frc.team5104.main;

import frc.team5104.util.Controller.Control;
import frc.team5104.util.Controller.ControlList;
import frc.team5104.util.Controller.Rumble;

/**
 * All the controls for the robot
 */
public class Controls {
	//Drive (in DriveController)
	public static final Control DRIVE_TURN = new Control(ControlList.LeftJoystickX);
	public static final Control DRIVE_FORWARD = new Control(ControlList.RightTrigger);
	public static final Control DRIVE_REVERSE = new Control(ControlList.LeftTrigger);
	public static final Control TOGGLE_VISION = new Control(ControlList.A);
	
	//IWE (in IWEController)
	public static final Control IWE_INTAKE = new Control(ControlList.X);
	public static final Control IWE_IDLE = new Control(ControlList.List); //single-tap -> idle, double-top -> manual mode
	public static final Control IWE_PLACE_EJECT = new Control(ControlList.B);
	public static final Control IWE_SWITCH_GAME_PIECE = new Control(ControlList.Y);
	public static final Control IWE_HEIGHT_L1 = new Control(ControlList.DirectionPadDown);
	public static final Control IWE_HEIGHT_L2 = new Control(ControlList.DirectionPadRight);
	public static final Control IWE_HEIGHT_L3 = new Control(ControlList.DirectionPadUp);
	public static final Control IWE_ELEVATOR_MANUAL = new Control(ControlList.RightJoystickY);
	public static final Control IWE_WRIST_MANUAL = new Control(ControlList.RightJoystickX);
	public static final Rumble IWE_INTAKE_RUMBLE = new Rumble(1, false, true, 500); //called in IWE directly
	public static final Rumble IWE_EJECT_RUMBLE = new Rumble(1, false, false, 500);
	public static final Rumble IWE_SWITCH_HATCH_RUMBLE = new Rumble(1, false, false, 500);
	public static final Rumble IWE_SWITCH_CARGO_RUMBLE = new Rumble(1, true, false, 500);
	public static final Rumble IWE_SWITCH_HEIGHT_RUMBLE = new Rumble(1, true, false, 500);
	
	//Compressor (in CompressorController)
	public static final Control COMPRESSOR_TOGGLE = new Control(ControlList.Menu);
}
