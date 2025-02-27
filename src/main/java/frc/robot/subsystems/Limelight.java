package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.DriveConstants;

public class Limelight extends SubsystemBase {
    private String limelightName;

    public Limelight(String limelightName){
        this.limelightName = limelightName;
    }
}