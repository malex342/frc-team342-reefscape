// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;

import frc.robot.commands.ExampleCommand;
import frc.robot.commands.Limelight.AutoAim;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.SwerveDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final XboxController operator;

  private final Wrist wrist;

  
  //Because the angles are the same for both L2 & L3, there will only be an L2 command that will be used for both
  

  private final JoystickButton lowFunnelButton;
  private final JoystickButton highFunnelButton;

  private final CommandXboxController m_driverController;
  private final ExampleSubsystem m_exampleSubsystem;
  
  // The robot's subsystems and commands are defined here...

  private SwerveDrive swerve;
  private Limelight limelight;
  private XboxController driver;
  private double tx;
  private JoystickButton fieldOrienatedButton;


  private Command fieldOrienatedCommand;


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // The robot's subsystems and commands are defined here...
    m_exampleSubsystem = new ExampleSubsystem();
  
    // Replace with CommandPS4Controller or CommandJoystick if needed
    m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
    wrist = new Wrist();
    boolean tv = LimelightHelpers.getTV("");
  if (!tv){
    tx = 0;
  }else{
    tx = LimelightHelpers.getTX("");
  }


    SmartDashboard.putData(wrist);

    operator = new XboxController(1);

    lowFunnelButton = new JoystickButton(operator, XboxController.Button.kLeftBumper.value);
    highFunnelButton = new JoystickButton(operator, XboxController.Button.kRightBumper.value);


    // Configure the trigger bindings
    

    swerve = new SwerveDrive();
    limelight = new Limelight("");
    
    driver = new XboxController(0);

    fieldOrienatedButton = new JoystickButton(driver, XboxController.Button.kY.value);
  

    fieldOrienatedCommand = Commands.runOnce(() -> {swerve.toggleFieldOriented();}, swerve);



    SmartDashboard.putData(swerve);

    configureBindings();

  }

/**
 * Use this method to define your trigger->command mappings. Triggers can be created via the
 * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
 * predicate, or via the named factories in {@link
 * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
 * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
 * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
 * joysticks}.
 */
private void configureBindings() {
 
  m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
  new Trigger(m_exampleSubsystem::exampleCondition)
      .onTrue(new ExampleCommand(m_exampleSubsystem));

  // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
  // cancelling on release.
  m_driverController.rightBumper().whileTrue(new AutoAim(limelight, swerve));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
  m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

  fieldOrienatedButton.whileTrue(fieldOrienatedCommand);
  }

  

}
