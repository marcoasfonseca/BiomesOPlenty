/*******************************************************************************
 * Copyright 2015-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package biomesoplenty.common.biome.overworld;

import net.minecraft.init.Blocks;
import biomesoplenty.api.biome.BOPBiome;
import biomesoplenty.api.biome.generation.GeneratorStage;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.enums.BOPClimates;
import biomesoplenty.common.world.BOPWorldSettings;
import biomesoplenty.common.world.feature.GeneratorOreSingle;

public class BiomeGenCrag extends BOPBiome
{    
    public BiomeGenCrag()
    {
        // terrain
        this.terrainSettings.avgHeight(80).heightVariation(80, 200).minHeight(40).sidewaysNoise(0.7F);
        
        this.setColor(5209457);
        this.setTemperatureRainfall(0.5F, 0.5F);

        this.canSpawnInBiome = false;
        this.canGenerateVillages = false;
        this.canGenerateRivers = false;
        
        this.beachBiomeId = -1;
        
        this.addWeight(BOPClimates.COLD_SWAMP, 1);

        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        
        this.topBlock = BOPBlocks.crag_rock.getDefaultState();
        this.fillerBlock = BOPBlocks.crag_rock.getDefaultState();
        this.waterColorMultiplier = 944693;
        this.skyColor = 4944498;
        
        this.avgDirtDepth = 8;
        
        // gem
        this.addGenerator("emeralds", GeneratorStage.SAND, (new GeneratorOreSingle.Builder()).amountPerChunk(12).with(Blocks.emerald_ore.getDefaultState()).create());
        
    }
    
    @Override
    public void applySettings(BOPWorldSettings settings)
    {
    	if (!settings.generateBopFoliage) {this.removeGenerator("bushes"); this.removeGenerator("koru"); this.removeGenerator("shrubs"); this.removeGenerator("leaf_piles"); this.removeGenerator("dead_leaf_piles"); this.removeGenerator("clover_patches"); this.removeGenerator("sprouts");}
    	
        if (!settings.generateBopPlants) {this.removeGenerator("cattail"); this.removeGenerator("double_cattail"); this.removeGenerator("river_cane"); this.removeGenerator("tiny_cacti"); this.removeGenerator("roots"); this.removeGenerator("rafflesia"); this.removeGenerator("desert_sprouts");}
    }
    
}
