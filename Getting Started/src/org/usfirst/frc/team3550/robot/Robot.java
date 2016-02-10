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
	RobotDrive myRobot;
	Joystick stick;
	int autoLoopCounter;
	SpeedController armMotorController;
	SpeedController MotorLateral1;
	SpeedController MotorLateral2;
	DigitalInput limitSwitch3, limitSwitch4, limitSwitch5, limitSwitchHaut, limitSwitchBas;	
	
	
	final double SPEED_ARMCONTROLLER = 0.7;
	final double SPEED_LATERALMOTOR  = 0.5;
	final double UP_ARMCONTROLLER    = -1.0;
	final double DOWN_ARMCONTROLLER  = 1.0;
	
	final boolean PASACTIVE_LSWITCHT = true;
	final boolean ACTIVE_LSWITCH     = false;
	final boolean PRESSED_BUTTON = true;
	final boolean UNPRESSED_BUTTON = false;
	
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(2,3,5,6);
    	stick = new Joystick(0);
    	armMotorController = new Talon(4);
    	MotorLateral1 = new Talon(0);
    	MotorLateral2 = new Talon (1);
    	limitSwitch3 = new DigitalInput(3);
    	limitSwitchHaut = new DigitalInput (1);
    	limitSwitchBas = new DigitalInput (7);
    	
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
    	if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
 
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        myRobot.arcadeDrive(stick);
        SmartDashboard.putBoolean("limitSwitchHaut", limitSwitchHaut.get());
        SmartDashboard.putBoolean("limitSwitch3", limitSwitch3.get());
        SmartDashboard.putBoolean("limitSwitchBas", limitSwitchBas.get());
        
        if (stick.getRawButton(3) ==  PRESSED_BUTTON && stick.getRawButton(10) == PRESSED_BUTTON && (limitSwitch3.get() == PASACTIVE_LSWITCHT) && (limitSwitchHaut.get() == PASACTIVE_LSWITCHT)) {
        	
			armMotorController.set(UP_ARMCONTROLLER*SPEED_ARMCONTROLLER); //monter
        }
        else if ((stick.getRawButton(4) == PRESSED_BUTTON || stick.getRawButton(9) == PRESSED_BUTTON) && (limitSwitchBas.get() == PASACTIVE_LSWITCHT)) {
			armMotorController.set(DOWN_ARMCONTROLLER*SPEED_ARMCONTROLLER); //descendre
		}
        else {
			armMotorController.set(0);
		}
        
        
        if (stick.getRawButton(5) == true) {
        	MotorLateral1.set(SPEED_LATERALMOTOR);
        	MotorLateral2.set(SPEED_LATERALMOTOR);
        }
        else if (stick.getRawButton(6) == true) {
        	MotorLateral1.set(-1*SPEED_LATERALMOTOR);
        	MotorLateral2.set(-1*SPEED_LATERALMOTOR);
        }
        else{
        	MotorLateral1.set(0);
        	MotorLateral2.set(0);           
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
}
