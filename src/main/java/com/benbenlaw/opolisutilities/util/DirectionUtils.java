package com.benbenlaw.opolisutilities.util;


import net.minecraft.core.Direction;

public class DirectionUtils {

    // Adjusts the direction to be relative to the front of the block
    public static Direction adjustPosition(Direction facing, Direction direction) {
        // Determine the direction of the left side based on the block's facing direction
        return switch (facing) {
            case NORTH, UP, DOWN -> direction; // Rotate the direction counterclockwise for north-facing blocks
            case SOUTH -> direction.getOpposite(); // Rotate the direction clockwise for south-facing blocks
            case EAST -> direction.getCounterClockWise(); // Reverse the direction for east-facing blocks
            case WEST -> direction.getClockWise(); // No adjustment needed for west-facing blocks
            default -> direction;
        };
    }
}