// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import static frc.robot.Constants.DriveConstants.*;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import frc.robot.subsystems.SwerveDrive;
import frc.robot.SwerveModule;

import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.controller.PIDController;

public class RotateToAngle extends Command {

  private SwerveDrive swerve; 
  private PIDController rotateController; 
  
  private double start; 
  private double end; 
  
  private double angle;
  private double current;


  /** Creates a new RotateToAngle. */
  public AHRS gyro;
  public RotateToAngle(double angle, SwerveDrive swerve) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.angle = angle;
    this.swerve = swerve; 
    addRequirements(swerve);

    rotateController = new PIDController(
     
      0.035,
      0.000, 
      0.000
    );

    rotateController.reset();
    rotateController.setTolerance(1);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  rotateController.enableContinuousInput(0, 360);
  start = 0;
  end = start + angle;
  gyro = new AHRS(NavXComType.kUSB1);
  }


  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    rotateController.setSetpoint(end);
    current = gyro.getAngle();
    double rotationSpeed = rotateController.calculate(current, end);
    ChassisSpeeds radial = new ChassisSpeeds(0, 0, rotationSpeed);
    //SwerveDriveKinematics.desaturateWheelSpeeds(swerve.getCurrentSwerveModulePositions(), MAX_ROTATE_SPEED * 0.3);
    swerve.drive(radial); 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    swerve.stopModules();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rotateController.atSetpoint();
  }
}
