package com.github.highfox.minimaltweaks;

import com.github.highfox.minimaltweaks.capability.BeaconBeamWrapper;
import com.github.highfox.minimaltweaks.capability.CapabilityHandler;
import com.github.highfox.minimaltweaks.network.event.VanillaPacketEvent;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SMountEntityPacket;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MTEvents {

	@SubscribeEvent
	public static void incomingPacket(VanillaPacketEvent.Incoming<SMountEntityPacket> event) {
		SMountEntityPacket packet = event.getPacket();
		if (packet.getVehicleEntityId() == -1 && MTConfig.leadBreakSound.get()) {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
				private static final long serialVersionUID = -2007016463658846889L;

				@Override
				public void run() {
					Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.5F, 2.0F));
				}
			});
		}
	}

	@SubscribeEvent
	public static void trampleCrops(BlockEvent.FarmlandTrampleEvent event) {
		if (event.getEntity() instanceof LivingEntity && MTConfig.featherFallingStopsTrampling.get()) {
			LivingEntity entity = (LivingEntity)event.getEntity();
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, entity.getItemStackFromSlot(EquipmentSlotType.FEET)) != 0) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void registerCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof BeaconTileEntity) {
			event.addCapability(new ResourceLocation(MinimalTweaks.MOD_ID, "beacon_beam"), new BeaconBeamWrapper());
		}
	}

	@SubscribeEvent
	public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
		if (event.getTarget() instanceof ItemFrameEntity && !((ItemFrameEntity)event.getTarget()).getDisplayedItem().isEmpty() && MTConfig.openChestsThroughItemFrames.get()) {
			PlayerEntity player = event.getPlayer();
			World world = player.world;
			if (!player.isSneaking()) {
				float f = player.rotationPitch;
				float f1 = player.rotationYaw;
				Vector3d vec3d = player.getEyePosition(1.0F);
				float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
				float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
				float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
				float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
				float f6 = f3 * f4;
				float f7 = f2 * f4;
				double d0 = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
				Vector3d vec3d1 = vec3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
				BlockRayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
				if (raytraceresult.getType() != RayTraceResult.Type.MISS && raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
					BlockPos pos = raytraceresult.getPos();
					TileEntity tileentity = world.getTileEntity(pos);
					if (tileentity instanceof ChestTileEntity) {
						player.openContainer((ChestTileEntity)tileentity);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntityLiving();
			ItemStack usedStack = event.getItem();
			ItemStack result = event.getResultStack();
			if (usedStack.getItem() instanceof PotionItem) {
				if (usedStack.getItem() == Items.POTION && MTConfig.autoStackUsedPotions.get()) {
					ItemHandlerHelper.giveItemToPlayer(player, result, -1);
				}
				event.setResultStack(ItemStack.EMPTY);
			}
		}
	}

	@SubscribeEvent
	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		ItemStack stack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);

		if ((stack.getItem() == Items.SOUL_SAND || stack.getItem() == Items.SOUL_SOIL) && !player.isSneaking()) {
			boolean flag = false;
			if (state.getBlock() == Blocks.LANTERN && MTConfig.enableLanternConversion.get()) {
				BlockState soulLantern = Blocks.SOUL_LANTERN.getDefaultState().with(LanternBlock.HANGING, state.get(LanternBlock.HANGING));
				world.setBlockState(pos, soulLantern);
				flag = true;
			} else if (state.getBlock() == Blocks.CAMPFIRE && MTConfig.enableCampfireConversion.get()) {
				BlockState soulCampfire = Blocks.SOUL_CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, state.get(CampfireBlock.LIT)).with(CampfireBlock.SIGNAL_FIRE, state.get(CampfireBlock.SIGNAL_FIRE)).with(CampfireBlock.WATERLOGGED, state.get(CampfireBlock.WATERLOGGED)).with(CampfireBlock.FACING, state.get(CampfireBlock.FACING));
				TileEntity oldTileEntity = world.getTileEntity(pos);
				TileEntity tileEntity = TileEntity.readTileEntity(state, oldTileEntity.write(new CompoundNBT()));
				if (oldTileEntity instanceof CampfireTileEntity) {
					((CampfireTileEntity)oldTileEntity).clear();
				}
				world.setBlockState(pos, soulCampfire);
				world.setTileEntity(pos, tileEntity);
				flag = true;
			} else if (state.getBlock() == Blocks.TORCH && MTConfig.enableTorchConversion.get()) {
				BlockState soulTorch = Blocks.SOUL_TORCH.getDefaultState();
				world.setBlockState(pos, soulTorch);
				flag = true;
			} else if (state.getBlock() == Blocks.WALL_TORCH && MTConfig.enableTorchConversion.get()) {
				BlockState wallSoulTorch = Blocks.SOUL_WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, state.get(WallTorchBlock.HORIZONTAL_FACING));
				world.setBlockState(pos, wallSoulTorch);
				flag = true;
			}

			if (flag) {
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				event.setUseItem(Result.DENY);
				world.playSound(player, pos, stack.getItem() == Items.SOUL_SAND ? SoundEvents.BLOCK_SOUL_SAND_PLACE : SoundEvents.BLOCK_SOUL_SOIL_PLACE, SoundCategory.BLOCKS, 1.5F, 1.0F);
			}
		}

		if (stack.getItem() == Items.IRON_BLOCK && (state.getBlock() == Blocks.CHIPPED_ANVIL || state.getBlock() == Blocks.DAMAGED_ANVIL) && MTConfig.repairableAnvils.get()) {
			BlockState newState = (state.getBlock() == Blocks.DAMAGED_ANVIL ? Blocks.CHIPPED_ANVIL.getDefaultState() : Blocks.ANVIL.getDefaultState()).getBlock().getDefaultState().with(AnvilBlock.FACING, state.get(AnvilBlock.FACING));
			world.setBlockState(pos, newState);

			if (!player.isCreative()) {
				stack.shrink(1);
			}
			event.setCanceled(true);
			event.setCancellationResult(ActionResultType.SUCCESS);
			world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 2.0F, 1.0F);
		}

		if (world.getTileEntity(pos) instanceof BeaconTileEntity && player.isSneaking()) {
			TileEntity tileentity = world.getTileEntity(pos);
			tileentity.getCapability(CapabilityHandler.BEACON_BEAM).ifPresent(instance -> instance.toggleBeamEnabled());
			tileentity.markDirty();
			event.setCanceled(true);
			event.setCancellationResult(ActionResultType.SUCCESS);
		}
	}

}
