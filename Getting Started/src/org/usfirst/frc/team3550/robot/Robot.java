package org.usfirst.frc.team3550.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive robotDrive;
	RobotDrive robotDriveInversed;
	Joystick stick;
	int autoLoopCounter;
	SpeedController frontLeftMotor;
	SpeedController rearLeftMotor;
	SpeedController frontRightMotor;
	SpeedController rearRightMotor;
	
	boolean inverseButtonWasPushed;
	boolean frontInversed;
	
	SpeedController armMotorController;
	SpeedController motorLateral1;
	SpeedController motorLateral2;
	DigitalInput limitSwitch3, limitSwitch4, limitSwitch5, limitSwitchHaut, limitSwitchBas;

	final static double SPEED_ARMCONTROLLER = 0.7;
	final static double SPEED_LATERALMOTOR = 0.5;
	final static double UP_ARMCONTROLLER = -1.0;
	final static double DOWN_ARMCONTROLLER = 1.0;

	final static boolean INACTIVE_LSWITCH = true;
	final static boolean ACTIVE_LSWITCH = false;
	final static boolean PRESSED_BUTTON = true;
	final static boolean UNPRESSED_BUTTON = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		frontLeftMotor = new Talon(2);
		rearLeftMotor = new Talon(3);
		frontRightMotor = new Talon(5);
		rearRightMotor = new Talon(6);
		
		frontLeftMotor.setInverted(true);
		rearLeftMotor.setInverted(true);
		frontRightMotor.setInverted(true);
		rearRightMotor.setInverted(true);
		
		robotDrive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		//robotDriveInversed = new RobotDrive(rearRightMotor, frontRightMotor, rearLeftMotor, frontLeftMotor);
		
		frontInversed = false;
		inverseButtonWasPushed = false;
		
		stick = new Joystick(0);
		armMotorController = new Talon(4);
		motorLateral1 = new Talon(0);
		motorLateral2 = new Talon(1);
		limitSwitch3 = new DigitalInput(3);
		limitSwitchHaut = new DigitalInput(1);
		limitSwitchBas = new DigitalInput(7);

		SmartDashboard.putBoolean("limitSwitchHaut", false);
		SmartDashboard.putBoolean("limitSwitch3", false);
		SmartDashboard.putBoolean("limitSwitchBas", false);
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	public void autonomousInit() {
		autoLoopCounter = 0;

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		// Check if we've completed 100 loops (approximately 2 seconds)
		if (autoLoopCounter < 100) {
			// drive forwards half speed
			robotDrive.drive(-0.5, 0.0); 
			autoLoopCounter++;
		} 
		else {
			robotDrive.drive(0.0, 0.0); // stop robot
		}
	}

	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		
		SmartDashboard.putBoolean("limitSwitchHaut", limitSwitchHaut.get());
		SmartDashboard.putBoolean("limitSwitch3", limitSwitch3.get());
		SmartDashboard.putBoolean("limitSwitchBas", limitSwitchBas.get());
		
		robotDrive.arcadeDrive(stick);

		if (stick.getRawButton(3) == PRESSED_BUTTON && stick.getRawButton(10) == PRESSED_BUTTON
				 && limitSwitchHaut.get() == INACTIVE_LSWITCH) {

			armMotorController.set(UP_ARMCONTROLLER * SPEED_ARMCONTROLLER); // monter
		} 
		else if ((stick.getRawButton(4) == PRESSED_BUTTON || stick.getRawButton(9) == PRESSED_BUTTON)
				&& limitSwitchBas.get() == INACTIVE_LSWITCH) {
			armMotorController.set(DOWN_ARMCONTROLLER * SPEED_ARMCONTROLLER); // descendre
		} 
		else {
			armMotorController.set(0);
		}

		if (stick.getRawButton(5) == true) {
			motorLateral1.set(SPEED_LATERALMOTOR);
			motorLateral2.set(SPEED_LATERALMOTOR);
		} 
		else if (stick.getRawButton(6) == true) {
			motorLateral1.set(-1 * SPEED_LATERALMOTOR);
			motorLateral2.set(-1 * SPEED_LATERALMOTOR);
		} 
		else {
			motorLateral1.set(0);
			motorLateral2.set(0);
		}
		
		if (stick.getRawButton(1) && !inverseButtonWasPushed) {
			robotDrive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
			inverseButtonWasPushed = true;
			
			if (!frontInversed) {
				frontInversed = true;
				robotDrive = new RobotDrive(rearRightMotor, frontRightMotor, rearLeftMotor, frontLeftMotor);
			}
			else {
				frontInversed = false;
				robotDrive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
			}
			robotDrive.arcadeDrive(stick);
		}
		else if (!stick.getRawButton(1)) {
			inverseButtonWasPushed = false;
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

}
