package com.benbenlaw.opolisutilities.util;


import net.minecraft.core.Direction;

public class DirectionUtils {
    public static Direction adjustPosition(Direction facing, Direction direction) {
        return switch (facing) {
            case NORTH, UP, DOWN -> direction;
            case SOUTH -> direction.getOpposite();
            case EAST -> direction.getClockWise();
            case WEST -> direction.getCounterClockWise();
            // Handle default case
        };
    }


}