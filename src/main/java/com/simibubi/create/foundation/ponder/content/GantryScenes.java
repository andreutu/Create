package com.simibubi.create.foundation.ponder.content;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.elements.WorldSectionElement;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GantryScenes {

	public static void introForPinion(SceneBuilder scene, SceneBuildingUtil util) {
		intro(scene, util, true);
	}

	public static void introForShaft(SceneBuilder scene, SceneBuildingUtil util) {
		intro(scene, util, false);
	}

	private static void intro(SceneBuilder scene, SceneBuildingUtil util, boolean pinion) {
		String id = "gantry_" + (pinion ? "carriage" : "shaft");
		String title = "Using Gantry " + (pinion ? "Carriages" : "Shafts");
		scene.title(id, title);
		
		scene.world.modifyKineticSpeed(util.select.everywhere(), f -> -2 * f);
		scene.configureBasePlate(0, 0, 5);
		scene.world.showSection(util.select.layer(0), Direction.UP);
		scene.idle(10);
		scene.world.showSection(util.select.layer(1), Direction.DOWN);
		scene.idle(10);
		ElementLink<WorldSectionElement> gantry =
			scene.world.showIndependentSection(util.select.layer(2), Direction.DOWN);
		scene.idle(10);

		BlockPos centralShaft = util.grid.at(2, 1, 2);

		scene.world.moveSection(gantry, util.vector.of(-4, 0, 0), 60);

		String text = pinion ? "Gantry Carriages can mount to and slide along a Gantry Shaft."
			: "Gantry Shafts form the basis of a gantry setup. Attached Carriages will move along them.";

		scene.overlay.showText(80)
			.text(text)
			.pointAt(util.vector.centerOf(centralShaft));

		scene.special.addKeyframe();
		scene.idle(80);

		scene.world.hideIndependentSection(gantry, Direction.UP);
		scene.idle(10);
		gantry = scene.world.showIndependentSection(util.select.layer(2), Direction.DOWN);
		Vec3d gantryTop = util.vector.topOf(4, 2, 2);
		scene.world.modifyKineticSpeed(util.select.everywhere(), f -> 0f);
		scene.overlay.showText(40)
			.text("Gantry setups can move attached Blocks.")
			.pointAt(gantryTop)
			.placeNearTarget();
		scene.special.addKeyframe();
		scene.idle(30);

		Selection planks = util.select.position(5, 3, 1);

		scene.world.showSectionAndMerge(util.select.layersFrom(3)
			.substract(planks), Direction.DOWN, gantry);
		scene.idle(10);
		scene.world.showSectionAndMerge(planks, Direction.SOUTH, gantry);
		scene.idle(10);
		scene.effects.superGlue(util.grid.at(5, 3, 1), Direction.SOUTH, true);

		scene.idle(20);
		scene.overlay.showText(80)
			.sharedText("movement_anchors")
			.pointAt(gantryTop)
			.placeNearTarget();
		scene.special.addKeyframe();
		scene.idle(80);

		scene.world.modifyKineticSpeed(util.select.layer(0), f -> 32f);
		scene.world.modifyKineticSpeed(util.select.layer(1), f -> -64f);

		scene.world.moveSection(gantry, util.vector.of(-4, 0, 0), 60);
		scene.idle(20);
		scene.markAsFinished();
	}

	public static void redstone(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("gantry_redstone", "Gantry Power Propagation");
		scene.world.modifyKineticSpeed(util.select.everywhere(), f -> -f);

		Selection leverRedstone = util.select.fromTo(3, 1, 0, 3, 1, 1);
		Selection shaft = util.select.fromTo(0, 1, 2, 4, 1, 2);
		Selection shaftAndCog = util.select.fromTo(0, 1, 2, 5, 1, 2);

		scene.configureBasePlate(0, 0, 5);
		scene.world.showSection(util.select.layer(0)
			.add(leverRedstone), Direction.UP);

		scene.idle(10);
		scene.world.showSection(shaftAndCog, Direction.DOWN);
		scene.idle(10);

		BlockPos gantryPos = util.grid.at(4, 2, 2);
		ElementLink<WorldSectionElement> gantry =
			scene.world.showIndependentSection(util.select.position(gantryPos), Direction.DOWN);
		scene.idle(15);
		scene.world.moveSection(gantry, util.vector.of(-3, 0, 0), 40);
		scene.idle(40);
		scene.special.addKeyframe();

		scene.world.toggleRedstonePower(shaft);
		scene.world.toggleRedstonePower(util.select.position(3, 1, 0));
		scene.world.toggleRedstonePower(util.select.position(3, 1, 1));
		scene.effects.indicateRedstone(util.grid.at(3, 1, 0));
		scene.world.modifyKineticSpeed(util.select.position(gantryPos), f -> 32f);
		scene.idle(40);

		BlockPos cogPos = util.grid.at(1, 2, 1);
		scene.overlay.showText(60)
			.colored(PonderPalette.RED)
			.pointAt(util.vector.centerOf(cogPos.down()
				.south()))
			.text("Redstone-powered gantry shafts stop moving their carriages")
			.placeNearTarget();
		scene.special.addKeyframe();
		scene.idle(70);

		Selection cogSelection = util.select.position(cogPos);
		scene.world.showSection(cogSelection, Direction.SOUTH);
		scene.world.modifyKineticSpeed(cogSelection, f -> 32f);
		scene.overlay.showText(180)
			.colored(PonderPalette.GREEN)
			.pointAt(util.vector.blockSurface(cogPos, Direction.NORTH))
			.text("Instead, its rotational force is relayed to the carriages' output shaft")
			.placeNearTarget();
		scene.idle(10);

		scene.effects.rotationSpeedIndicator(cogPos);
		scene.markAsFinished();
	}

	public static void direction(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("gantry_direction", "Gantry Movement Direction");
		scene.configureBasePlate(0, 0, 5);
		scene.world.modifyKineticSpeed(util.select.everywhere(), f -> -f);
		scene.world.showSection(util.select.layer(0), Direction.UP);
		scene.idle(10);

		Selection shaftAndGearshiftAndLever = util.select.fromTo(0, 1, 2, 5, 2, 2);
		Selection shafts = util.select.fromTo(0, 1, 2, 3, 1, 2);

		scene.world.showSection(shaftAndGearshiftAndLever, Direction.DOWN);
		scene.overlay.showText(60)
			.text("Gantry Shafts can have opposite orientations")
			.pointAt(util.vector.of(2, 1.5, 2.5))
			.placeNearTarget();
		scene.idle(60);

		ElementLink<WorldSectionElement> gantry1 =
			scene.world.showIndependentSection(util.select.position(0, 1, 3), Direction.NORTH);
		ElementLink<WorldSectionElement> gantry2 =
			scene.world.showIndependentSection(util.select.position(3, 1, 3), Direction.NORTH);
		scene.idle(10);

		scene.world.moveSection(gantry1, util.vector.of(1, 0, 0), 20);
		scene.world.moveSection(gantry2, util.vector.of(-1, 0, 0), 20);

		scene.overlay.showText(80)
			.text("The movement direction of carriages depend on their shafts' orientation")
			.pointAt(util.vector.topOf(1, 1, 3))
			.placeNearTarget();
		scene.special.addKeyframe();
		scene.idle(80);

		BlockPos lastShaft = util.grid.at(0, 1, 2);
		boolean flip = true;

		for (int i = 0; i < 3; i++) {
			scene.world.modifyBlocks(util.select.fromTo(4, 1, 2, 4, 2, 2), s -> s.cycle(BlockStateProperties.POWERED),
				false);
			scene.effects.indicateRedstone(util.grid.at(4, 2, 2));
			scene.world.moveSection(gantry1, util.vector.of(flip ? -1 : 1, 0, 0), 20);
			scene.world.moveSection(gantry2, util.vector.of(flip ? 1 : -1, 0, 0), 20);
			scene.world.modifyKineticSpeed(shafts, f -> -f);
			scene.effects.rotationDirectionIndicator(lastShaft.east(flip ? 1 : 0));
			scene.idle(20);

			if (i == 0) {
				scene.overlay.showText(80)
					.text("...as well as the rotation direction of the shaft")
					.pointAt(util.vector.blockSurface(lastShaft, Direction.WEST))
					.placeNearTarget();
				scene.special.addKeyframe();
			}

			scene.idle(30);
			flip = !flip;
		}

		Selection kinetics = util.select.fromTo(0, 2, 3, 3, 3, 3);
		Selection gears1 = util.select.fromTo(0, 1, 3, 0, 3, 3);
		Selection gears2 = util.select.fromTo(3, 1, 3, 3, 3, 3);

		scene.world.showSection(kinetics, Direction.DOWN);
		scene.world.showSection(util.select.fromTo(0, 1, 0, 4, 1, 1), Direction.SOUTH);
		scene.idle(20);

		BlockPos leverPos = util.grid.at(4, 1, 0);
		scene.world.modifyBlocks(util.select.fromTo(1, 1, 0, 3, 1, 1),
			s -> s.has(RedstoneWireBlock.POWER) ? s.with(RedstoneWireBlock.POWER, 15) : s, false);
		scene.world.toggleRedstonePower(util.select.position(leverPos));
		scene.world.toggleRedstonePower(shafts);
		scene.effects.indicateRedstone(leverPos);
		scene.world.modifyKineticSpeed(gears1, f -> -32f);
		scene.world.modifyKineticSpeed(gears2, f -> 32f);

		scene.idle(20);
		scene.overlay.showText(120)
			.text("Same rules apply for the propagated rotation")
			.pointAt(util.vector.topOf(0, 3, 3))
			.placeNearTarget();
		scene.special.addKeyframe();
		scene.idle(20);

		for (boolean flip2 : Iterate.trueAndFalse) {
			scene.effects.rotationDirectionIndicator(util.grid.at(0, 3, 3));
			scene.effects.rotationDirectionIndicator(util.grid.at(3, 3, 3));

			scene.idle(60);
			scene.world.modifyBlocks(util.select.fromTo(4, 1, 2, 4, 2, 2), s -> s.cycle(BlockStateProperties.POWERED),
				false);
			scene.effects.indicateRedstone(util.grid.at(4, 2, 2));
			scene.world.modifyKineticSpeed(gears1, f -> -f);
			scene.world.modifyKineticSpeed(gears2, f -> -f);

			if (!flip2) {
				scene.effects.rotationDirectionIndicator(util.grid.at(0, 3, 3));
				scene.effects.rotationDirectionIndicator(util.grid.at(3, 3, 3));
				scene.markAsFinished();
			}
		}

	}

	public static void subgantry(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("gantry_cascaded", "Cascaded Gantries");
		scene.configureBasePlate(0, 0, 5);
		scene.world.modifyKineticSpeed(util.select.everywhere(), f -> -2 * f);
		scene.world.showSection(util.select.layer(0)
			.add(util.select.column(5, 3))
			.add(util.select.fromTo(2, 1, 3, 4, 1, 3)), Direction.UP);
		scene.idle(10);

		BlockPos gantryPos = util.grid.at(5, 1, 2);
		BlockPos gantryPos2 = util.grid.at(3, 2, 2);
		ElementLink<WorldSectionElement> gantry =
			scene.world.showIndependentSection(util.select.position(gantryPos), Direction.SOUTH);
		scene.idle(5);

		scene.world.showSectionAndMerge(util.select.fromTo(0, 1, 2, 4, 1, 2), Direction.EAST, gantry);
		scene.idle(15);

		scene.world.moveSection(gantry, util.vector.of(0, 2, 0), 40);
		scene.overlay.showText(60)
			.text("Gantry shafts attach to a carriage without the need of super glue")
			.independent(20);
		scene.special.addKeyframe();
		scene.idle(40);

		scene.world.modifyKineticSpeed(util.select.everywhere(), f -> -f);
		scene.world.moveSection(gantry, util.vector.of(0, -2, 0), 40);
		scene.idle(40);

		ElementLink<WorldSectionElement> secondGantry =
			scene.world.showIndependentSection(util.select.position(gantryPos2), Direction.DOWN);
		scene.idle(15);
		scene.overlay.showText(60)
			.text("Same applies for carriages on moved Gantry Shafts")
			.independent(20);
		scene.special.addKeyframe();
		scene.idle(15);

		scene.world.moveSection(gantry, util.vector.of(0, 2, 0), 40);
		scene.world.moveSection(secondGantry, util.vector.of(0, 2, 0), 40);

		scene.idle(40);
		BlockPos leverPos = util.grid.at(2, 1, 3);
		scene.world.toggleRedstonePower(util.select.position(leverPos));
		scene.world.toggleRedstonePower(util.select.fromTo(3, 1, 3, 4, 1, 3));
		scene.world.toggleRedstonePower(util.select.fromTo(5, 1, 3, 5, 4, 3));
		scene.world.modifyKineticSpeed(util.select.fromTo(0, 1, 2, 5, 1, 2), f -> -32f);
		scene.effects.indicateRedstone(leverPos);
		scene.world.moveSection(secondGantry, util.vector.of(-3, 0, 0), 60);

		scene.idle(20);
		scene.overlay.showText(120)
			.text("Thus, a gantry system can be cascaded to cover multiple axes of movement")
			.independent(20);
	}

}