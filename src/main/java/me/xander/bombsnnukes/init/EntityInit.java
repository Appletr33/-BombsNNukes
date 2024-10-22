package me.xander.bombsnnukes.init;

import me.xander.bombsnnukes.BombsnNukes;
import me.xander.bombsnnukes.Entities.PrimedNukeEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, BombsnNukes.MOD_ID);

    public static final RegistryObject<EntityType<PrimedNukeEntity>> PRIMED_NUKE = ENTITIES.register("primed_nuke",
            () -> EntityType.Builder.<PrimedNukeEntity>of(PrimedNukeEntity::new, EntityClassification.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("primed_nuke"));
}