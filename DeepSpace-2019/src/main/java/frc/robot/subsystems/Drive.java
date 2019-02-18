/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.DriveXOne;

/**
 * <h1>Drive</h1>
 * Moves the robot between two places. Always uses the Xbox ONE controller
 * command unless explicitly told otherwise
 */
public class Drive extends Subsystem {
	private double leftSpeed, rightSpeed; //Makes math easier for fancy drive

	private static ADIS16448_IMU imu;
	private static Encoder encLeft, encRight; //TODO: adjust for circumference
	private static Talon talonLeft, talonRight;
	private static TalonSRX mantisLeft, mantisRight;

	/**
	 * Set up the Talons and encoders with the ports specified
	 * in {@link RobotMap}, and the IMU with the Y-axis as yaw
	 */
	public Drive() {
		imu = new ADIS16448_IMU(ADIS16448_IMU.Axis.kZ); //The parameter here is the axis the IMU interprets as being yaw. This will depend on how the RIO is oriented

		encLeft = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_CHANNEL_A, RobotMap.DRIVE_ENCODER_LEFT_CHANNEL_B);
		encRight = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_CHANNEL_A, RobotMap.DRIVE_ENCODER_RIGHT_CHANNEL_B);

		talonLeft = new Talon(RobotMap.DRIVE_TALON_LEFT_CHANNEL);
		talonRight = new Talon(RobotMap.DRIVE_TALON_RIGHT_CHANNEL);

		talonLeft.setInverted(true); //Negate all speeds to the left side to account for mirrored axes

		mantisLeft = new TalonSRX(RobotMap.DRIVE_MANTIS_SRX_LEFT_ID);
		mantisRight = new TalonSRX(RobotMap.DRIVE_MANTIS_SRX_RIGHT_ID);

		mantisRight.setInverted(true);
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveXOne());
	}

	/**
	 * @return left encoder raw count
	 */
	public int getLeftEncoder() {
		return encLeft.get();
	}

	/**
	 * @return right encoder raw count
	 */
	public int getRightEncoder() {
		return encRight.get();
	}

	/**
	 * @return encoder-derived left rail speed
	 */
	public double getLeftSpeed() {
		return encLeft.getRate();
	}

	/**
	 * @return encoder-derived right rail speed
	 */
	public double getRightSpeed() {
		return encRight.getRate();
	}

	/**
	 * @return left encoder distance
	 */
	public double getLeftDistance() {
		return encLeft.getDistance();
	}

	/**
	 * @return right encoder distance
	 */
	public double getRightDistance() {
		return encRight.getDistance();
	}

	/**
	 * Zero the left encoder
	 */
	public void resetLeftEncoder() {
		encLeft.reset();
	}

	/**
	 * Zero the right encoder
	 */
	public void resetRightEncoder() {
		encRight.reset();
	}

	/**
	 * Zero both encoders
	 */
	public void resetBothEncoders() {
		encLeft.reset();
		encRight.reset();
	}

	/**
	 * Set the speed, as a percentage of max forward speed, of the left drive rail
	 * @param speed percentage of max forward speed
	 */
	public void setDriveLeft(double speed) {
		if(speed < -1) {
			speed = -1;
		} else if (speed > 1) {
			speed = 1;
		}

		talonLeft.set(speed);
	}

	/**
	 * Set the speed, as a percentage of max forward speed, of the right drive rail
	 * @param speed percentage of max forward speed
	 */
	public void setDriveRight(double speed) {
		if(speed < -1) {
			speed = -1;
		} else if (speed > 1) {
			speed = 1;
		}

		talonRight.set(speed);
	}

	/**
	 * Set the drive train to a given speed, as a percentage of max forward speed<br>
	 * Both rails will be at equal speed
	 */
	public void setDriveBoth(double speed) {
		setDriveLeft(speed);
		setDriveRight(speed);
	}

	/**
	 * Set the drive train to a given speed, as a percentage of max forward speed<br>
	 * Rails are independently sped
	 */
	public void setDriveBoth(double lSpeed, double rSpeed) {
		setDriveLeft(lSpeed);
		setDriveRight(rSpeed);
	}

	/**
	 * Stop the drive train
	 */
	public void stopDrive() {
		setDriveBoth(0);
	}

	/**
	 * Get the robot's yaw angle
	 */
	public double getRotation() {
		return imu.getYaw();
	}

	/**
	 * Zero the IMU
	 */
	public void imuReset() {
		imu.reset();
	}

	/**
	 * Set mantis left wheel's speed
	 * @param speed percentage [-1,1]
	 */
	public void setMantisLeft(double speed) {
		mantisLeft.set(ControlMode.PercentOutput, speed);
	}

	/**
	 * Set mantis right wheel's speed
	 * @param speed percentage [-1,1]
	 */
	public void setMantisRight(double speed) {
		mantisRight.set(ControlMode.PercentOutput, speed);
	}

	/**
	 * Set mantis both wheels' speeds
	 * @param speed percentage [-1,1]
	 */
	public void setMantisBoth(double speed) {
		setMantisLeft(speed);
		setMantisRight(speed);
	}

	/**
	 * Set mantis both wheels' speeds, indepentently
	 * @param lSpeed left wheel percentage [-1,1]
	 * @param rSpeed right wheel percentage [-1,1]
	 */
	public void setMantisBoth(double lSpeed, double rSpeed) {
		setMantisLeft(lSpeed);
		setMantisRight(rSpeed);
	}

	/**
     * One joystick drive mode. One stick axis speeds forward/backwards, the other adds rotation on robot yaw axis
     * @param moveValue forward/backward speed, as a percentage of max forward speed
     * @param rotateValue rotation speed, as a percentage of max rightward rotation speed
     */
    public void arcadeDrive(double moveValue, double rotateValue) {
		if(moveValue > 0.0) {
		    if(rotateValue > 0.0) {
		    	leftSpeed = moveValue - rotateValue;
		    	rightSpeed = Math.max(moveValue, rotateValue);
		    } else {
		    	leftSpeed = Math.max(moveValue, -rotateValue);
		    	rightSpeed = moveValue + rotateValue;
		    }
		} else {
		    if(rotateValue > 0.0) {
		    	leftSpeed = -Math.max(-moveValue, rotateValue);
		    	rightSpeed = moveValue + rotateValue;
		    } else {
		    	leftSpeed = moveValue - rotateValue;
		    	rightSpeed = -Math.max(-moveValue, -rotateValue);
		    }
		}

		if(!Robot.isFrontHigh() && !Robot.isBackHigh()) {
			setMantisBoth(0, 0);
			setDriveBoth(leftSpeed, rightSpeed);
		} else {
			setMantisBoth(leftSpeed, rightSpeed);
			setDriveBoth(leftSpeed, rightSpeed);
		}
	}

	/**
	 * Experiental GTA-esque drive scheme from 2017. Probably not for competition use
	 * @param speed forward/backward speed, as a percentage of max forward speed
	 * @param rotateValue rotation speed, as a percentage of max rightward rotation speed
	 */
	// public void gtaDrive(double speed, double rotateValue) {
	// 	if(speed > 0) {
	// 		if(rotateValue > 0) {
	// 			//
	// 		}
	// 	}
	// }
}