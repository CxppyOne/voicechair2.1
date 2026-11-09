package com.example.voicechair2;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Jurystuhl.
 * - Rechtsklick (leere Hand): hinsetzen
 * - Rechtsklick mit Buzzer: diesen Stuhl umdrehen / zurueckdrehen
 * - TURNED=false: roter Stuhl, TURNED=true: umgedreht, mit weissem Rand
 */
public class VoiceChairBlock extends Block {
	public static final MapCodec<VoiceChairBlock> CODEC = simpleCodec(VoiceChairBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty TURNED = BooleanProperty.create("turned");
	private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

	public VoiceChairBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TURNED, false));
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TURNED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		// Stuhl schaut in dieselbe Richtung wie der Spieler: man sieht zuerst die Rueckseite.
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(TURNED, false);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
										  Player player, InteractionHand hand, BlockHitResult hit) {
		if (stack.getItem() instanceof BuzzerItem) {
			if (level instanceof ServerLevel serverLevel) {
				toggleOne(serverLevel, pos, state);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
											   Player player, BlockHitResult hit) {
		if (level instanceof ServerLevel serverLevel) {
			SeatManager.sit(serverLevel, pos, player);
		}
		return InteractionResult.SUCCESS;
	}

	/** Setzt den Zustand ohne Fliesen-Update. Gibt true zurueck, wenn sich etwas geaendert hat. */
	public static boolean applyTurned(ServerLevel level, BlockPos pos, BlockState state, boolean turned, boolean sound) {
		if (state.getValue(TURNED) == turned) return false;
		Direction newFacing = state.getValue(FACING).getOpposite();
		level.setBlock(pos, state.setValue(TURNED, turned).setValue(FACING, newFacing), 3);
		if (sound) {
			level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 1.0f, 0.7f);
		}
		SeatManager.rotate(pos, newFacing.toYRot());
		return true;
	}

	/** Einzelner Stuhl: umdrehen und Namensfliesen in der Naehe aktualisieren. */
	public static void toggleOne(ServerLevel level, BlockPos pos, BlockState state) {
		applyTurned(level, pos, state, !state.getValue(TURNED), true);
		NameTileBlock.refreshAround(level, pos);
	}
}
