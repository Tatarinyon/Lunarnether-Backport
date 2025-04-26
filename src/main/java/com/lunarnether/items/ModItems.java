package com.lunarnether.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModItems {

    // TitaniumIngot sınıfı içindeki registry ataması sayesinde getRegistryName() dolu olacak.
    public static final Item TITANIUM_INGOT = new TitaniumIngot();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // Tüm item’leri kayıt altına alıyoruz.
        event.getRegistry().registerAll(TITANIUM_INGOT);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        // Model kayıt işlemini Forge'un ModelLoader yöntemiyle gerçekleştiriyoruz.
        registerModel(TITANIUM_INGOT);
    }

    private static void registerModel(Item item) {
        if (item.getRegistryName() != null) {
            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory")
            );
        } else {
            System.err.println("registerModel: Registry name is null for item: " + item);
        }
    }
}
