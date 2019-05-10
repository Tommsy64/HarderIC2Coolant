/*
 * A tiny mod to remove the IC2 coolent recipe that uses regular water.
 * Copyright (C) 2019  Thomas Pakh
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see LICENSE.md at the root of the project.
 */
package com.tommsy.hardercoolent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import ic2.api.recipe.ICannerEnrichRecipeManager.Input;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import lombok.Getter;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HarderCoolent.MOD_ID, name = HarderCoolent.MOD_NAME, version = HarderCoolent.VERSION)
public class HarderCoolent {
    public static final String MOD_ID = "@MOD_ID@";
    public static final String MOD_NAME = "@MOD_NAME@";
    public static final String VERSION = "@VERSION@";

    @Getter
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Iterable<? extends MachineRecipe<Input, FluidStack>> recipes = Recipes.cannerEnrich.getRecipes();

        Iterator<? extends MachineRecipe<Input, FluidStack>> iterator = recipes.iterator();
        while (iterator.hasNext()) {
            MachineRecipe<Input, FluidStack> recipe = iterator.next();
            if (recipe.getOutput().getFluid().getName().equals("ic2coolant") && recipe.getInput().fluid.getFluid().getName().equals("water")) {
                iterator.remove();
                logger.info("Successfully removed coolent recipe.");
                return;
            }
        }

        ArrayList<String> inputFluids = new ArrayList<>(), outputFluids = new ArrayList<>();
        iterator = recipes.iterator();
        while (iterator.hasNext()) {
            MachineRecipe<Input, FluidStack> recipe = iterator.next();
            inputFluids.add(recipe.getInput().fluid.getFluid().getName());
            outputFluids.add(recipe.getOutput().getFluid().getName());
        }
        logger.error("Could not find coolent recipe.");
        Collector<CharSequence, ?, String> joiner = Collectors.joining(",", "[", "]");
        logger.error("Avaiable canner enrichment input fluids: {}", inputFluids.stream().collect(joiner));
        logger.error("Avaiable canner enrichment output fluids: {}", outputFluids.stream().collect(joiner));
    }
}
