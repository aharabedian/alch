package gg.clerisy.alch.common.init;

import gg.clerisy.alch.AlchMod;
import gg.clerisy.alch.common.StillBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AlchMod.MODID);

    public static final RegistryObject<BlockEntityType<StillBlockEntity>> STILL = BLOCK_ENTITY_TYPES.register("still", () -> BlockEntityType.Builder.of(StillBlockEntity::new, BlockInit.STILL.get()).build(null));
}
