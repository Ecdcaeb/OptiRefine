package rml.loader.deserialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import rml.deserializer.AbstractDeserializer;
import rml.deserializer.JsonDeserializeException;
import rml.jrx.announces.BeDiscovered;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.utils.RandomHolder;
import rml.jrx.utils.values.RandomIntSupplier;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/20 22:20
 **/
@BeDiscovered(BeDiscovered.PRE_INIT)
public class MCDeserializers {
    /*
     * MC BuildIn
     */
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final AbstractDeserializer<NBTTagCompound> NBT_TAG_COMPOUND = Deserializer.MANAGER.addDefaultEntry(
            new AbstractDeserializer<>(new ResourceLocation("minecraft", "json_to_nbt"), NBTTagCompound.class,
                    element -> {
                        try {
                            if (element.isJsonObject())
                                return JsonToNBT.getTagFromJson(GSON.toJson(element));
                            else
                                return JsonToNBT.getTagFromJson(element.getAsString());
                        }catch (NBTException e){
                            throw new JsonDeserializeException(element, e);
                        }
                    })
    );

    public static final AbstractDeserializer<ItemStack> ITEM_STACK_DEFAULT = Deserializer.named(ItemStack.class, new ResourceLocation("minecraft", "item"))
            .require(Item.class, "item")
            .optionalWhen(Integer.class, "data", context -> context.get(Item.class, "item").getHasSubtypes())
            .check((context -> {if (!context.ifPresent("data")) context.put("data", 0); return null;}))
            .optional(NBTTagCompound.class, "nbt")
            .optionalDefault(Integer.class, "count", 1)
            .decode((context -> {
                Item item = context.get(Item.class, "item");
                int data = context.get(Integer.class, "data");
                int count = context.get(Integer.class, "count");
                if (context.ifPresent(NBTTagCompound.class, "nbt")){
                    NBTTagCompound nbt = context.get(NBTTagCompound.class, "nbt");
                    NBTTagCompound tmp = new NBTTagCompound();
                    if (nbt.hasKey("ForgeCaps"))
                    {
                        tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"));
                        nbt.removeTag("ForgeCaps");
                    }

                    tmp.setTag("tag", nbt);
                    tmp.setString("id", item.getRegistryName().toString());
                    tmp.setInteger("Count", count);
                    tmp.setInteger("Damage", data);

                    return new ItemStack(tmp);
                }else {
                    return new ItemStack(item, count, data);
                }
            })).markDefault().build();

    public static final AbstractDeserializer<EnchantmentData> ENCHANTMENT_DATA = Deserializer.named(EnchantmentData.class, new ResourceLocation("minecraft", "enchantment_data"))
            .require(Enchantment.class, "name")
            .require(RandomIntSupplier.class, "level")
            .decode(context -> new EnchantmentData(context.get(Enchantment.class, "name"), context.get(RandomIntSupplier.class, "level").get(RandomHolder.RANDOM)))
            .markDefault().build();

    public static final AbstractDeserializer<ITextComponent> ITEXT_COMPONENT = Deserializer.MANAGER.addDefaultEntry(new AbstractDeserializer<>(new ResourceLocation("minecraft", "textcomponent"), ITextComponent.class, AbstractDeserializer.safeRun((jsonElement -> ITextComponent.Serializer.jsonToComponent(String.valueOf(jsonElement))))));


    // Build Deserializer for all IForgeRegistry
    @PrivateAPI
    public static <T extends IForgeRegistryEntry<T>> IForgeRegistry<T> onNewRegistry(final IForgeRegistry<T> registry, final ResourceLocation name) {
        AbstractDeserializer<T> abstractDeserializer = new AbstractDeserializer<>(name, registry.getRegistrySuperType(), jsonElement -> {
            ResourceLocation resourceLocation = Deserializer.decode(ResourceLocation.class, jsonElement);
            T obj = registry.getValue(resourceLocation);
            if (obj != null) return obj;
            else throw new JsonDeserializeException(jsonElement, "No Such RegistryEntry for " + name);
        });
        Deserializer.MANAGER.addDefaultEntry(abstractDeserializer);
        return registry;
    }
}
