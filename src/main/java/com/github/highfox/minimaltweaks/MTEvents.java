package com.github.highfox.minimaltweaks;

import com.github.highfox.minimaltweaks.util.BeaconBeamInternals;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MTEvents {
	public static MTConfig CONFIG = AutoConfig.getConfigHolder(MTConfig.class).getConfig();

	public static ActionResult onTrampleCrops(World world, BlockPos pos, LivingEntity entity) {
		if (EnchantmentHelper.getEquipmentLevel(Enchantments.FEATHER_FALLING, entity) != 0) {
			return ActionResult.FAIL;
		}

		return ActionResult.PASS;
	}

	public static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getStackInHand(hand);
		BlockPos pos = hitResult.getBlockPos();
		BlockState state = world.getBlockState(pos);

		if (!player.isSpectator()) {
			if ((stack.getItem() == Items.SOUL_SAND || stack.getItem() == Items.SOUL_SOIL) && !player.isSneaking()) {
				boolean flag = false;
				if (state.getBlock() == Blocks.LANTERN && CONFIG.soulBlockConversion.enableLanternConversion) {
					BlockState soulLantern = Blocks.SOUL_LANTERN.getDefaultState().with(LanternBlock.HANGING, state.get(LanternBlock.HANGING));
					world.setBlockState(pos, soulLantern);
					flag = true;
				} else if (state.getBlock() == Blocks.CAMPFIRE && CONFIG.soulBlockConversion.enableCampfireConversion) {
					BlockState soulCampfire = Blocks.SOUL_CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, state.get(CampfireBlock.LIT)).with(CampfireBlock.SIGNAL_FIRE, state.get(CampfireBlock.SIGNAL_FIRE)).with(CampfireBlock.WATERLOGGED, state.get(CampfireBlock.WATERLOGGED)).with(CampfireBlock.FACING, state.get(CampfireBlock.FACING));
					BlockEntity oldBlockEntity = world.getBlockEntity(pos);
					BlockEntity blockEntity = BlockEntity.createFromTag(state, oldBlockEntity.toTag(new CompoundTag()));
					if (oldBlockEntity instanceof CampfireBlockEntity) {
						((CampfireBlockEntity)oldBlockEntity).clear();
					}
					world.setBlockState(pos, soulCampfire);
					world.setBlockEntity(pos, blockEntity);
					flag = true;
				} else if (state.getBlock() == Blocks.TORCH && CONFIG.soulBlockConversion.enableTorchConversion) {
					BlockState soulTorch = Blocks.SOUL_TORCH.getDefaultState();
					world.setBlockState(pos, soulTorch);
					flag = true;
				} else if (state.getBlock() == Blocks.WALL_TORCH && CONFIG.soulBlockConversion.enableTorchConversion) {
					BlockState wallSoulTorch = Blocks.SOUL_WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING));
					world.setBlockState(pos, wallSoulTorch);
					flag = true;
				}

				if (flag) {
					if (!player.isCreative()) {
						stack.decrement(1);
					}
					world.playSound(player, pos, stack.getItem() == Items.SOUL_SAND ? SoundEvents.BLOCK_SOUL_SAND_PLACE : SoundEvents.BLOCK_SOUL_SOIL_PLACE, SoundCategory.BLOCKS, 1.5F, 1.0F);
					return ActionResult.SUCCESS;
				}
			}

			if (stack.getItem() == Items.IRON_BLOCK && (state.getBlock() == Blocks.CHIPPED_ANVIL || state.getBlock() == Blocks.DAMAGED_ANVIL) && CONFIG.repairableAnvils) {
				BlockState newState = (state.getBlock() == Blocks.DAMAGED_ANVIL ? Blocks.CHIPPED_ANVIL.getDefaultState() : Blocks.ANVIL.getDefaultState()).getBlock().getDefaultState().with(AnvilBlock.FACING, state.get(AnvilBlock.FACING));
				world.setBlockState(pos, newState);

				if (!player.isCreative()) {
					stack.decrement(1);
				}
				world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 2.0F, 1.0F);
				return ActionResult.SUCCESS;
			}

			if (world.getBlockEntity(pos) instanceof BeaconBlockEntity && player.isSneaking()) {
				BeaconBlockEntity blockEntity = (BeaconBlockEntity)world.getBlockEntity(pos);
				BeaconBeamInternals beaconBeam = (BeaconBeamInternals)blockEntity;
				beaconBeam.setBeaconBeamEnabled(!beaconBeam.beaconBeamEnabled());
				blockEntity.markDirty();
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

}
