/* BreakerBots Robotics Team (FRC 5104) 2020 */
package frc.team5104.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import frc.team5104.Constants;
import frc.team5104.util.DriveSignal;
import frc.team5104.util.Units;
import frc.team5104.util.managers.Subsystem;

public class Drive extends Subsystem {
	protected String getName() { return "Drive"; }

	private static TalonSRX talonL1, talonL2, talonR1, talonR2;
	private static PigeonIMU gyro;
	
	//External Functions
	public static void set(DriveSignal signal) { currentDriveSignal = signal; }
	public static void stop() { currentDriveSignal = new DriveSignal(); }

	//Loop
	private static DriveSignal currentDriveSignal = new DriveSignal();
	protected void update() {
		shift(currentDriveSignal.isHighGear);
		switch (currentDriveSignal.unit) {
			case PERCENT_OUTPUT: {
				setMotors(
						currentDriveSignal.leftSpeed, 
						currentDriveSignal.rightSpeed, 
						ControlMode.PercentOutput,
						currentDriveSignal.feedForward
					);
				break;
			}
			case FEET_PER_SECOND: {
				setMotors(
						Units.feetPerSecondToTalonVel(currentDriveSignal.leftSpeed), 
						Units.feetPerSecondToTalonVel(currentDriveSignal.rightSpeed), 
						ControlMode.Velocity,
						currentDriveSignal.feedForward
					);
				break;
			}
			case VOLTAGE: {
				setMotors(
						currentDriveSignal.leftSpeed / getLeftGearboxVoltage(),
						currentDriveSignal.rightSpeed / getRightGearboxVoltage(),
						ControlMode.PercentOutput,
						currentDriveSignal.feedForward
					);
				break;
			}
			case STOP:
				stopMotors();
				break;
		}
		currentDriveSignal = new DriveSignal();
	}
	
	//Internal Functions
	static void setMotors(double leftSpeed, double rightSpeed, ControlMode controlMode, double feedForward) {
		talonL1.set(controlMode, leftSpeed, DemandType.ArbitraryFeedForward, feedForward);
		talonR1.set(controlMode, rightSpeed, DemandType.ArbitraryFeedForward, feedForward);
	}
	static void stopMotors() {
		talonL1.set(ControlMode.Disabled, 0);
		talonR1.set(ControlMode.Disabled, 0);
	}
	static void shift(boolean toHigh) { /*shifter.set(toHigh ? Value.kForward : Value.kReverse);*/ }
	static boolean getShift() { return true;/*shifter.get() == Value.kForward;*/ }
	
	//External Functions
	public static double getLeftGearboxVoltage() { return talonL1.getBusVoltage(); }
	public static double getRightGearboxVoltage() { return talonR1.getBusVoltage(); }
	public static double getLeftGearboxOutputVoltage() { return talonL1.getMotorOutputVoltage(); }
	public static double getRightGearboxOutputVoltage() { return talonR1.getMotorOutputVoltage(); }
	public static void resetGyro() { gyro.addYaw(getGyro()); }
	public static double getGyro() {
		double[] ypr = new double[3];
		gyro.getYawPitchRoll(ypr); 
		return -ypr[0];
	}
	public static void resetEncoders() {
		talonL1.setSelectedSensorPosition(0);
		talonR1.setSelectedSensorPosition(0);
	}
	public static int getLeftEncoderPositionRaw() {
		return talonL1.getSelectedSensorPosition();
	}
	public static int getRightEncoderPositionRaw() {
		return talonR1.getSelectedSensorPosition();
	}
	
	//Config
	protected void init() {
		talonL1 = new TalonSRX(11);
		talonL2 = new TalonSRX(12);
		talonR1 = new TalonSRX(13);
		talonR2 = new TalonSRX(14);
		gyro = new PigeonIMU(talonR2);
		
		talonL1.configFactoryDefault();
		talonL2.configFactoryDefault();
		talonL1.config_kP(0, Constants.DRIVE_KP, 0);
		talonL1.config_kI(0, Constants.DRIVE_KI, 0);
		talonL1.config_kD(0, Constants.DRIVE_KD, 0);
		talonL1.config_kF(0, 0, 0);
		talonL2.set(ControlMode.Follower, talonL1.getDeviceID());
		talonL1.setInverted(true);
		talonL2.setInverted(true);
		
		talonR1.configFactoryDefault();
		talonR2.configFactoryDefault();
		talonR1.config_kP(0, Constants.DRIVE_KP, 0);
		talonR1.config_kI(0, Constants.DRIVE_KI, 0);
		talonR1.config_kD(0, Constants.DRIVE_KD, 0);
		talonR1.config_kF(0, 0, 0);
		talonR2.set(ControlMode.Follower, talonR1.getDeviceID());
		
		stopMotors();
		resetGyro();
		resetEncoders();
	}
	protected void enabled() {
		currentDriveSignal = new DriveSignal();
	}
	protected void disabled() {
		currentDriveSignal = new DriveSignal();
	}
}