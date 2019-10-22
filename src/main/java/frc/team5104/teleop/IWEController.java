package frc.team5104.teleop;

import frc.team5104.main.Controls;
import frc.team5104.statemachines.IWE;
import frc.team5104.statemachines.IWE.IWEControl;
import frc.team5104.statemachines.IWE.IWEGamePiece;
import frc.team5104.statemachines.IWE.IWEHeight;
import frc.team5104.statemachines.IWE.IWEState;
import frc.team5104.util.console;
import frc.team5104.util.console.c;
import frc.team5104.util.managers.TeleopController;

public class IWEController extends TeleopController {
	protected String getName() { return "IWE Controller"; }

	protected void update() {
		//IWE State
		if (Controls.IWE_IDLE.getPressed()) {
			console.log(c.IWE, "idling");
			IWE.setState(IWEState.IDLE);
		}
		if (Controls.IWE_INTAKE.getPressed()) {
			console.log(c.IWE, "intaking");
			IWE.setState(IWEState.INTAKE);
		}
		if (Controls.IWE_PLACE_EJECT.getPressed()) {
			if (IWE.getState() == IWEState.IDLE) {
				console.log(c.IWE, "placing");
				IWE.setState(IWEState.PLACE);
			}
			else if (IWE.getState() != IWEState.EJECT) {
				console.log(c.IWE, "ejecting");
				IWE.setState(IWEState.EJECT);
				Controls.IWE_EJECT_RUMBLE.start();
			}
		}
		
		//IWE Game Piece
		if (Controls.IWE_SWITCH_GAME_PIECE.getPressed()) {
			IWE.setGamePiece(IWE.getGamePiece() == IWEGamePiece.HATCH ? IWEGamePiece.CARGO : IWEGamePiece.HATCH);
			console.log(c.IWE, "switcing game piece to " + IWE.getGamePiece().toString().toLowerCase());
			if (IWE.getGamePiece() == IWEGamePiece.HATCH)
				Controls.IWE_SWITCH_HATCH_RUMBLE.start();
			else
				Controls.IWE_SWITCH_CARGO_RUMBLE.start();
		}
		
		//IWE Height
		if (Controls.IWE_HEIGHT_L1.getPressed()) {
			console.log(c.IWE, "setting target height to L1");
			IWE.setHeight(IWEHeight.L1);
			Controls.IWE_SWITCH_HEIGHT_RUMBLE.start();
		}
		if (Controls.IWE_HEIGHT_L2.getPressed()) {
			console.log(c.IWE, "setting target height to L2");
			IWE.setHeight(IWEHeight.L2);
			Controls.IWE_SWITCH_HEIGHT_RUMBLE.start();
		}
		if (Controls.IWE_HEIGHT_L3.getPressed()) {
			console.log(c.IWE, "setting target height to L3");
			IWE.setHeight(IWEHeight.L3);
			Controls.IWE_SWITCH_HEIGHT_RUMBLE.start();
		}
		
		//IWE Control
		if (Controls.IWE_IDLE.getDoubleClick() == 2) {
			IWE.setControl(IWE.getControl() == IWEControl.AUTONOMOUS ? IWEControl.MANUAL : IWEControl.AUTONOMOUS);
			console.log(c.IWE, "setting control mode to " + IWE.getControl().toString().toLowerCase());
		}
		IWE.desiredWristManaul = Controls.IWE_WRIST_MANUAL.getAxis();
		IWE.desiredElevatorManaul = Controls.IWE_ELEVATOR_MANUAL.getAxis();
	}
}
