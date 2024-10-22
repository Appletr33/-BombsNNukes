package me.xander.bombsnnukes.Registers;

import me.xander.bombsnnukes.BombsnNukes;
import me.xander.bombsnnukes.Renderer.PrimedNukeEntityRenderer;
import me.xander.bombsnnukes.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BombsnNukes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.PRIMED_NUKE.get(), PrimedNukeEntityRenderer::new);
    }
}