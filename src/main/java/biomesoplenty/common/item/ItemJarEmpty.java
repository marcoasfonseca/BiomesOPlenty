/*******************************************************************************
 * Copyright 2014-2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package biomesoplenty.common.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.entities.EntityPixie;


public class ItemJarEmpty extends Item
{
    
    public ItemJarEmpty()
    {
        this.setMaxDamage(0);
    }
        
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        
        MovingObjectPosition hit = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (hit == null) {return stack;}
        if (hit.typeOfHit != MovingObjectType.BLOCK) {return stack;}
        BlockPos pos = hit.getBlockPos();
        if (!world.isBlockModifiable(player, pos)) {return stack;}
        if (!player.canPlayerEdit(pos, hit.sideHit, stack)) {return stack;}       
        
        // determine if the block is one of our BOP fluids
        // note - no need to check level - you don't get a hit unless it's full
        IBlockState state = world.getBlockState(pos);
        ItemJarFilled.JarContents jarContents = null;
        if (state.getBlock() == BOPBlocks.honey)
        {
            jarContents = ItemJarFilled.JarContents.HONEY;                
        }
        else if (state.getBlock() == BOPBlocks.poison)
        {
            jarContents = ItemJarFilled.JarContents.POISON;
        }
        
        // if it was honey or poison, return the corresponding filled jar
        if (jarContents != null)
        {
            world.setBlockToAir(pos);
            --stack.stackSize;
            player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            
            ItemStack honeyJar = new ItemStack(BOPItems.jar_filled, 1, jarContents.ordinal());
            // if there was only one empty jar in the stack, replace it, otherwise add the filledJar elsewhere in the inventory
            if (stack.stackSize <= 0)
            {
                return honeyJar;
            }
            else if (!player.inventory.addItemStackToInventory(honeyJar))
            {
                // no room in inventory, so just drop it on the floor
                player.dropPlayerItemWithRandomChoice(honeyJar, false);
            }
        }
            
        return stack;
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target)
    {
        // right clicking a pixie with an empty jar catches it in the jar
        if (target instanceof EntityPixie)
        {
            EntityPixie pixie = (EntityPixie)target;
            pixie.setDead();
            --stack.stackSize;
            ItemStack pixieJar = new ItemStack(BOPItems.jar_filled, 1, ItemJarFilled.JarContents.PIXIE.ordinal());
            EntityItem pixieJarEntity = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, pixieJar);
            if (!player.worldObj.isRemote)
            {
                player.worldObj.spawnEntityInWorld(pixieJarEntity);
                if (!(player instanceof FakePlayer)) {pixieJarEntity.onCollideWithPlayer(player);}
            }
            return true;
        }
        return false;
    }
    
}
  