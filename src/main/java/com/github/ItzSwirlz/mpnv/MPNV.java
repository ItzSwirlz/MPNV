package com.github.ItzSwirlz.mpnv;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.*;

public class MPNV implements ModInitializer {
	public class CalculationResults {
		public boolean isPrime = true;
		public boolean isMersennePrime = false;

		public CalculationResults(int num) {
			/*
			 * Regular primality test
			 * src: https://dev.to/priyanka__488/number-theory-primality-test-in-o-sqrt-n-dde
			 * 
			 * By default, assume the number is prime.
			 */ 
			if(num == 1) isPrime = false;
			
			for (int i = 2; i * i < num; i++) {
				if (num % i == 0) isPrime = false;
			}
			// If the number is prime, isPrime will still be true.

			if(isPrime) {
				/*
				 * Mersenne Primality Test
				 */
				if((Math.log(num + 1) / Math.log(2)) % 1 == 0) isMersennePrime = true;
			}
		}
	}

	public static boolean isPrime(int num) {
		if(num == 1) return false;

		for (int i = 2; i * i < num; i++) {
			if (num % i == 0) return false;
		}

		return true;
	}

	@Override
	public void onInitialize(ModContainer mod) {
		// FIXME: this needs massive cleanup
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("mpnv2d")
			.then(argument("operation", StringArgumentType.string())
			.then(argument("radius", IntegerArgumentType.integer())
        	.executes(context -> {
				final String operation = StringArgumentType.getString(context, "operation");
				final int radius = IntegerArgumentType.getInteger(context, "radius");

				for(int x = 0; x < radius; x++) {
					CalculationResults calculationResultsX = new CalculationResults(x);
					if(operation.equals("and")) {
						for(int z = 0; z < radius; z++) {
							CalculationResults calculationResultsZ = new CalculationResults(z);
							if(calculationResultsX.isPrime && calculationResultsZ.isPrime) {
								System.out.println("(" + x + ", " + z + ") is x prime: " + calculationResultsX.isPrime + "is z prime: " + calculationResultsZ.isPrime + " is x mersenne: " + calculationResultsX.isMersennePrime + " is z mersenne: " + calculationResultsZ.isMersennePrime);
								if(calculationResultsX.isMersennePrime && calculationResultsZ.isMersennePrime) {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.GOLD_BLOCK.getDefaultState());
								} else {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
					else if(operation.equals("or")) {
						for(int z = 0; z < radius; z++) {
							CalculationResults calculationResultsZ = new CalculationResults(z);
							if(calculationResultsX.isPrime || calculationResultsZ.isPrime) {
								if(calculationResultsX.isMersennePrime || calculationResultsZ.isMersennePrime) {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.GOLD_BLOCK.getDefaultState());
								} else {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
					else if(operation.equals("neither") || operation.equals("nor")) {
						for(int z = 0; z < radius; z++) {
							CalculationResults calculationResultsZ = new CalculationResults(z);
							if(!calculationResultsX.isPrime && !calculationResultsZ.isPrime) {
								if(!calculationResultsX.isMersennePrime && !calculationResultsZ.isMersennePrime) {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.GOLD_BLOCK.getDefaultState());
								} else {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
					else if(operation.equals("plus")) {
						for(int z = 0; z < radius; z++) {
							CalculationResults calculationResults = new CalculationResults(x + z);
							if(calculationResults.isPrime) {
								if(calculationResults.isMersennePrime) {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.GOLD_BLOCK.getDefaultState());
								} else {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
					else if(operation.equals("subtract")) {
						for(int z = 0; z < radius; z++) {
							CalculationResults calculationResults = new CalculationResults(x - z);
							if(calculationResults.isPrime) {
								if(calculationResults.isMersennePrime) {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.GOLD_BLOCK.getDefaultState());
								} else {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
					else if(operation.equals("divide")) {
						for(int z = 1; z < radius; z++) {
							CalculationResults calculationResults = new CalculationResults(x / z);
							if(calculationResults.isPrime) {
								if(calculationResults.isMersennePrime) {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.GOLD_BLOCK.getDefaultState());
								} else {
									context.getSource().getWorld().setBlockState(new BlockPos(x, 0, z), Blocks.STONE.getDefaultState());
								}
							}
						}
					}
				}
				return 0;
		})))));

			// for my own personal use
			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("clearworld")
        	.executes(context -> {
				for(int x = 0; x < 1000; x++) {
					for(int z = 0; z < 1000; z++) {
						context.getSource().getWorld().removeBlock(new BlockPos(x, 0, z), false);
					}	
				}
				return 0;
	    	})));
	}
}
