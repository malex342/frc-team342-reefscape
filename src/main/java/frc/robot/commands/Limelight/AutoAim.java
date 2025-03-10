package frc.robot.commands.Limelight;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.Constants;
import static frc.robot.Constants.DriveConstants.*;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

public class AutoAim extends Command{
  private SwerveDrive swerve;
  private Limelight limelight;
  private PIDController rotateController;

  private double start;
  private double end;

  private double tx;
  private boolean tv;
  private double current;

  public AHRS gyro;

  public AutoAim(Limelight limelight, SwerveDrive swerve){
    this.limelight = limelight;
    this.swerve = swerve;
    addRequirements(swerve);
    addRequirements(limelight);

    rotateController = new PIDController(
      ROTATE_P_VALUE, 
      ROTATE_I_VALUE, 
      ROTATE_D_VALUE
    );

    rotateController.reset();
    rotateController.setTolerance(2);
  }

   // Called when the command is initially scheduled.
   @Override
   public void initialize() {
    rotateController.enableContinuousInput(0, 360);
    start = 0;
    tv = LimelightHelpers.getTV("limey");

    //check for target
    if (!tv){
        tx = 0;
        System.out.println("Who turned out the lights?");
      }else{
        tx = LimelightHelpers.getTX("limey");
        System.out.println("I be seeing the April Tag");
    }
    System.out.println("tv="+tv+" tx="+tx);
    
    end = start + tx;
    gyro = swerve.getGyro();
   }

   // Called every time the scheduler runs while the command is scheduled.
   @Override
   public void execute() {
    rotateController.setSetpoint(end);
    System.out.println("setpoint set X3");
    current = gyro.getAngle();
    System.out.println("angle got got");
    System.out.println("current: "+current); //gets what the current angle is?
    double rotationSpeed = rotateController.calculate(current, end);
    ChassisSpeeds radial = new ChassisSpeeds(0, 0, rotationSpeed);
    swerve.drive(radial); //make robot go zoom
    System.out.println("we think we drove somewhere!! XP");
    System.out.println("rotationspeed="+rotationSpeed);
   }

   // Called once the command ends or is interrupted.
   @Override
   public void end(boolean interrupted) {
    swerve.stopModules();
    System.out.println("modules stopped! X3");
   }

// Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println("im done! :3");
    return rotateController.atSetpoint();
  }
}
