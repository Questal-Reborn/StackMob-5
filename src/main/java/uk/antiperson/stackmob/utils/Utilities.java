package uk.antiperson.stackmob.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

public class Utilities<T> {

    public static final Component PREFIX = Component.text("StackMob ").color(TextColor.color(0, 206, 209)).append(Component.text(">> ").color(NamedTextColor.GRAY)).compact();
    public static final String PREFIX_STRING = LegacyComponentSerializer.legacySection().serialize(PREFIX);
    public static final String SLIME_METADATA = "deathcount";
    public static final String NO_LEASH_METADATA = "stop-leash";
    public static final String DISCORD = "https://discord.gg/fz9xzuB";
    public static final String GITHUB = "https://github.com/Nathat23/StackMob-5";
    public static final String GITHUB_DEFAULT_CONFIG = GITHUB + "/tree/master/src/main/resources";
    private static final boolean usingPaper;
    public static final List<Material> DROWNED_MATERIALS = Arrays.asList(Material.NAUTILUS_SHELL, Material.TRIDENT);
    public static final List<EquipmentSlot> HAND_SLOTS = Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    private static MinecraftVersion minecraftVersion;
    public static final MinecraftVersion NMS_VERSION = MinecraftVersion.V1_19_R1;
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().character('&').hexColors().hexCharacter('#').build();

    static {
        boolean usingPaper1;
        try {
            Bukkit.spigot().getClass().getMethod("getPaperConfig");
            usingPaper1 = true;
        } catch (NoSuchMethodException e) {
            usingPaper1 = false;
        }
        usingPaper = usingPaper1;
    }

    public static Component createComponent(String toTranslate) {
        return legacyComponentSerializer.deserialize(toTranslate);
    }

    public static <B> B[] arrayRange(B[] array, int startIndex, int endIndex) {
        // Create array of appropriate length
        B[] result = (B[])(new Vector<B>(array.length - startIndex - (array.length - endIndex)).toArray());

        for (int i = startIndex; i < endIndex; i++) {
            result[i - startIndex] = array[i];
        }

        return result;
    }

    public static List<Integer> split(int dividend, int divisor) {
        int fullAmount = dividend / divisor;
        int remainder = dividend % divisor;
        List<Integer> numbers = new ArrayList<>(fullAmount + 1);
        for (int i = 0; i < fullAmount; i++) {
            numbers.add(divisor);
        }
        if (remainder > 0) {
            numbers.add(remainder);
        }
        return numbers;
    }

    public static CompletableFuture<DownloadResult> downloadFile(File filePath, String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL webPath = new URL(url);
                try (InputStream in = webPath.openStream()) {
                    Files.createDirectories(filePath.getParentFile().toPath());
                    Files.copy(in, filePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                return DownloadResult.SUCCESSFUL;
            } catch (IOException e) {
                e.printStackTrace();
                return DownloadResult.ERROR;
            }
        });
    }

    public static boolean isPaper() {
        return usingPaper;
    }

    public static MinecraftVersion getMinecraftVersion() {
        if (minecraftVersion == null) {
            minecraftVersion = MinecraftVersion.V1_16_R1;
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String ending = packageName.substring(packageName.lastIndexOf('.') + 1);
            for (MinecraftVersion version: MinecraftVersion.values()) {
                if (version.getInternalName().equals(ending)){
                    minecraftVersion = version;
                }
            }
        }
        return minecraftVersion;
    }

    public static boolean isVersionAtLeast(MinecraftVersion version) {
        return getMinecraftVersion().ordinal() >= version.ordinal();
    }

    public static boolean isDye(ItemStack material) {
        return material.getType().toString().endsWith("_DYE");
    }

    public static void removeHandItem(Player player, int itemAmount) {
        if (itemAmount == player.getInventory().getItemInMainHand().getAmount()) {
           player.getInventory().setItemInMainHand(null);
           return;
        }
        ItemStack is = player.getInventory().getItemInMainHand();
        is.setAmount(is.getAmount() - itemAmount);
        player.getInventory().setItemInMainHand(is);
    }

    public enum DownloadResult {
        SUCCESSFUL,
        ERROR
    }

    public enum MinecraftVersion {
        V1_16_R1("v1_16_R1"),
        V1_17_R1("v1_17_R1"),
        V1_18_R1("v1_18_R1"),
        V1_18_R2("v1_18_R2"),
        V1_19_R1("v1_19_R1");

        String internalName;

        MinecraftVersion(String internalName) {
            this.internalName = internalName;
        }

        public String getInternalName() {
            return internalName;
        }
    }
}
