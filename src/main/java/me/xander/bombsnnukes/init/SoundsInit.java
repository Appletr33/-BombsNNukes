package me.xander.bombsnnukes.init;

import me.xander.bombsnnukes.BombsnNukes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundsInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BombsnNukes.MOD_ID);

    public static final RegistryObject<SoundEvent> SOUND_NUCLEAR_EXPLOSION = sound("nuke_explosion");

    private static RegistryObject<SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(BombsnNukes.MOD_ID,  name)));
    }
}
