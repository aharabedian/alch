package gg.clerisy.alch.common.init;

import gg.clerisy.alch.AlchMod;
import gg.clerisy.alch.common.StillMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AlchMod.MODID);

    public static final RegistryObject<MenuType<StillMenu>> STILL = MENU_TYPES.register("still", () -> new MenuType<>(StillMenu::new));
}
