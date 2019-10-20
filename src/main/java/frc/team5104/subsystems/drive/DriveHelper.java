/*BreakerBots Robotics Team 2019*/
package frc.team5104.subsystems.drive;

import frc.team5104.subsystems.drive.DriveObjects.DriveSignal;
import frc.team5104.subsystems.drive.DriveObjects.DriveUnit;
import frc.team5104.util.BezierCurve;
import frc.team5104.util.BezierCurveInterpolator;
import frc.team5104.util.Deadband;
import frc.team5104.util.Deadband.deadbandType;

/** A class for better handling of the robot's drive train (PLAZZZ MAKE BETTEER) */
public class DriveHelper {
	
	//Constants
	private static final double DRIVE_CURVE_CHANGE = 1.0;
	private static final double TURN_CURVE_SPEED_ADJUST = 0.2;
	
	private static final double TURN_DEADBAND = 0.08;
	private static final double FORWARD_DEADBAND = 0.01;
	
	private static final double RIGHT_ACCOUNT_FORWARD = 1.000;
	private static final double RIGHT_ACCOUNT_REVERSE = 1.000;
	private static final double LEFT_ACCOUNT_FORWARD  = 1.000;
	private static final double LEFT_ACCOUNT_REVERSE  = 1.000;
	
	private static final double MIN_SPEED_HIGH_GEAR_FORWARD = 0;
	private static final double MIN_SPEED_HIGH_GEAR_TURN = 0;
	private static final double MIN_SPEED_LOW_GEAR_FORWARD = 0;
	private static final double MIN_SPEED_LOW_GEAR_TURN = 0;
	
	private static final double KICKSTAND_SCALAR_FORWARD = 0.25;
	private static final double KICKSTAND_SCALAR_TURN = 0.25;
	
	//Objects
	private static final BezierCurve driveCurve = new BezierCurve(.2, 0, .2, 1);
	private static final BezierCurve turnCurve = new BezierCurve(0.15, 0.7, 0.8, 0.225);
	private static final BezierCurveInterpolator leftSpeedCurve  = new BezierCurveInterpolator(DRIVE_CURVE_CHANGE, driveCurve);
	private static final BezierCurveInterpolator rightSpeedCurve = new BezierCurveInterpolator(DRIVE_CURVE_CHANGE, driveCurve);
	
	//Methods
	public static double applyKickstandForward(double forward) { return forward * KICKSTAND_SCALAR_FORWARD; }
	public static double applyKickstandTurn(double turn) { return turn * KICKSTAND_SCALAR_TURN; }
	
	/** Calculates and left and right speed (in volts) for the robot depending on input variables */
	public static DriveSignal get(double turn, double forward, boolean inHighGear) {
		//deadbands
		turn = Deadband.get(turn, TURN_DEADBAND, deadbandType.slopeAdjustment);
		forward = Deadband.get(forward, FORWARD_DEADBAND, deadbandType.slopeAdjustment);
		
		//bezier curve
		turnCurve.x1 = (1 - Math.abs(forward)) * (1 - TURN_CURVE_SPEED_ADJUST) + TURN_CURVE_SPEED_ADJUST;
		turn = turnCurve.getPoint(turn);
		
		//inertia
		leftSpeedCurve.setSetpoint(forward - turn);
		rightSpeedCurve.setSetpoint(forward + turn);
		DriveSignal signal = new DriveSignal(
			leftSpeedCurve.update() * 12, 
			rightSpeedCurve.update() * 12, 
			DriveUnit.voltage
		);
		
		//drive straight
		signal = applyDriveStraight(signal);
		
		//min speed
		signal = applyMotorMinSpeed(signal, inHighGear);
		
		return signal;
	}
	
	private static DriveSignal applyDriveStraight(DriveSignal signal) {
		double leftMult = (signal.leftSpeed > 0 ? 
				LEFT_ACCOUNT_REVERSE : 
				LEFT_ACCOUNT_FORWARD
			);
		double rightMult = (signal.rightSpeed > 0 ? 
				RIGHT_ACCOUNT_REVERSE : 
				RIGHT_ACCOUNT_FORWARD
			);
		signal.leftSpeed = signal.leftSpeed * leftMult;
		signal.rightSpeed = signal.rightSpeed * rightMult;
		return signal;
	}

	private static DriveSignal applyMotorMinSpeed(DriveSignal signal, boolean inHighGear) {
		double turn = Math.abs(signal.leftSpeed - signal.rightSpeed) / 2;
		double biggerMax = (Math.abs(signal.leftSpeed) > Math.abs(signal.rightSpeed) ? Math.abs(signal.leftSpeed) : Math.abs(signal.rightSpeed));
		if (biggerMax != 0)
			turn = Math.abs(turn / biggerMax);
		double forward = 1 - turn;
		
		double minSpeed;
		if (inHighGear)
			minSpeed = (forward * (MIN_SPEED_HIGH_GEAR_FORWARD/12.0)) + (turn * (MIN_SPEED_HIGH_GEAR_TURN/12.0));
		else
			minSpeed = (forward * (MIN_SPEED_LOW_GEAR_FORWARD/12.0)) + (turn * (MIN_SPEED_LOW_GEAR_TURN/12.0));
		
		if (signal.leftSpeed != 0)
			signal.leftSpeed = signal.leftSpeed * (1 - minSpeed) + (signal.leftSpeed > 0 ? minSpeed : -minSpeed);
		if (signal.rightSpeed != 0)
			signal.rightSpeed = signal.rightSpeed * (1 - minSpeed) + (signal.rightSpeed > 0 ? minSpeed : -minSpeed);

		return signal;
	}
}