/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.manual;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ManualKaChunkerRelease extends Command {
  public ManualKaChunkerRelease() {
	  requires(Robot.manipulatorWithKaChunker);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
	  Robot.manipulatorWithKaChunker.setKachunkerDrop();
	  System.out.println("[" + getClass().getName() + "] -Initialize-");
	  System.out.println("[" + getClass().getName() + "] -KaChunkerGrab-");
  }
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
	  System.out.println("[" + getClass().getName() + "] -Execute-");
	  System.out.println("[" + getClass().getName() + "] -KaChunkerRelease- " + Robot.elevator.getSpeed());
	  System.out.println("[" + getClass().getName() + "] -KaChunkerGrab- "  + Robot.elevator.getPos());
  }
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
