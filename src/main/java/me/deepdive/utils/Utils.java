package me.deepdive.utils;


import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.NonNull;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class provides a lot of static smaller utility methods
 * to help you out in general, like colorizing methods, formatting currencies,
 * a bunch of vector math utilities, converting ItemStack's to Base64,
 * progress bar methods, converting enum types (such as enchantments) from input strings,
 * setting world borders, and much more
 */
public class Utils {


    public static void registerCommand(String command, Object commandClass){
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command, (Command) commandClass);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static UUID getOfflineUUID(String playerName){
        if(playerName.contains(":") || playerName.contains("*") || playerName.contains("&") ||
                playerName.contains("^") || playerName.contains("%") || playerName.contains("$") || playerName.contains("-") ||
                playerName.contains(",") || playerName.contains("+") || playerName.contains("=") || playerName.contains("'") ||
                playerName.contains("\"") || playerName.contains("|") || playerName.contains("\\") || playerName.contains(";") ||
                playerName.contains("<") || playerName.contains(">") || playerName.contains("/") || playerName.contains("?") ||
                playerName.contains("!") || playerName.contains("@") || playerName.contains("#")){
            return null;
        }

        return Bukkit.getOfflinePlayer(playerName).getUniqueId();
    }

    public static OfflinePlayer getOfflinePlayer(String playerName){
        Player p = Bukkit.getPlayer(playerName);
        if(p != null) return p;

        if(playerName.contains(":") || playerName.contains("*") || playerName.contains("&") ||
                playerName.contains("^") || playerName.contains("%") || playerName.contains("$") || playerName.contains("-") ||
                playerName.contains(",") || playerName.contains("+") || playerName.contains("=") || playerName.contains("'") ||
                playerName.contains("\"") || playerName.contains("|") || playerName.contains("\\") || playerName.contains(";") ||
                playerName.contains("<") || playerName.contains(">") || playerName.contains("/") || playerName.contains("?") ||
                playerName.contains("!") || playerName.contains("@") || playerName.contains("#")){
            return null;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(!(player.hasPlayedBefore())) return null;
        return player;
    }

    public static Player getPlayer(String username){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getName().equalsIgnoreCase(username)) return p;
        }

        return null;
    }

    public static Player getPlayer(UUID uuid){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getUniqueId().equals(uuid)) return p;
        }

        return null;
    }


    public static String integerToRoman(int amount){
        switch(amount) {
            case 1 -> {
                return "I";
            }
            case 2 -> {
                return "II";
            }
            case 3 -> {
                return "III";
            }
            case 4 -> {
                return "IV";
            }
            case 5 -> {
                return "V";
            }
            case 6 -> {
                return "VI";
            }
            case 7 -> {
                return "VII";
            }
            case 8 -> {
                return "VIII";
            }
            case 9 -> {
                return "XI";
            }
            case 10 -> {
                return "X";
            }
            default -> {return "";}
        }
    }

    public static String getRandomString(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public static LivingEntity getNearestEntity(Location l){
        Collection<LivingEntity> entities = l.getNearbyLivingEntities(500);
        LivingEntity entity = null;
        double distance = Double.MAX_VALUE;
        for(LivingEntity e : entities){
            if(l.distance(e.getLocation()) < distance){
                entity = e;
            }
        }

        return entity;
    }


    public static ItemStack fromBytes(byte[] bytes){
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(bytes);
            BukkitObjectInputStream ois = new BukkitObjectInputStream(baip);
            return (ItemStack)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(ItemStack item){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(stream);
            out.writeObject(item);
        }catch(IOException e){
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(stream.toByteArray());
        return bais.readAllBytes();
    }

    public static String toBase64(ItemStack item){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(outputStream);
            bukkitObjectOutputStream.writeObject(item);
            bukkitObjectOutputStream.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemStack itemFromBase64(String item){
        try{
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(item));
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(inputStream);

            ItemStack item1 = (ItemStack) bukkitObjectInputStream.readObject();
            bukkitObjectInputStream.close();
            return item1;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBase64(List<ItemStack> items){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(outputStream);
            bukkitObjectOutputStream.writeObject(items);
            bukkitObjectOutputStream.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ItemStack> fromBase64(String item){
        if(item.equalsIgnoreCase("")){
            return new ArrayList<>();
        }
        try{
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(item));
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(inputStream);

            List<ItemStack> items = (List<ItemStack>) bukkitObjectInputStream.readObject();
            bukkitObjectInputStream.close();
            return items;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int calculateLargestNum(int[] array, int total){
        int cal;
        for (int i = 0; i < total; i++)
        {
            for (int j = i + 1; j < total; j++)
            {
                if (array[i] >array[j])
                {
                    cal =array[i];
                    array[i] = array[j];
                    array[j] = cal;
                }
            }
        }
        return array[total-1];
    }

    private static final Pattern pattern2 = Pattern.compile("&#[a-fA-F0-9]{6}");
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String getHex(String msg) {
        msg = getHexWithSymbol(msg);
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = pattern.matcher(msg);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String getHexWithSymbol(String msg){
        Matcher matcher = pattern2.matcher(msg);
        while (matcher.find()) {
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color.replace("&", "")) + "");
            matcher = pattern2.matcher(msg);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static String c(String s){
        return ChatColor.translateAlternateColorCodes('&', getHex(s));
    }

    public static List<String> cL(List<String> strings){
        List<String> newList = new ArrayList<>();
        for(String s : strings){
            newList.add(c(s));
        }
        return newList;
    }

    private static String removeSign(String s, boolean value) {
        if (value)
            s = s.replaceAll("\\$", "");
        return s;
    }

    private static String trimLastDigit(String s, boolean removeSign) {
        String[] split = s.split("\\.", 2);
        String decimal = split[1].substring(0, Math.min(split[1].length(), 1));
        if (decimal.length() == 0) decimal += "0";

        return removeSign(split[0] + "." + decimal, removeSign);
    }

    public static String prettyMoney(Double balance, boolean removeSign, boolean longName) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

        int i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign);

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Thousand" : "K");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Million" : "M");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Billion" : "B");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Trillion" : "T");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Quadrillion" : "Q");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Quintillion" : "Qt");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Sextillion" : "SX");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Septillion" : "SP");

        balance = Math.floor(balance / 100) / 10.0;

        i = balance.intValue() / 1000;

        if (i == 0)
            return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Octillion" : "O");

        balance = Math.floor(balance / 100) / 10.0;
        return trimLastDigit(formatter.format(balance), removeSign) + (longName ? " Nonillion" : "N");
    }

    public static byte[] itemsToSQL(Map<ItemStack, Integer> items){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(stream);
            out.writeObject(items);
            return stream.toByteArray();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getProgressBar(double current, double requirement, int totalBars, char symbol) {
        float percent = (float) (current / requirement);
        int progressBars = (int) (totalBars * percent);
        if(current > requirement){
            progressBars = totalBars;
        }
        if(totalBars - progressBars <=0){
            return ChatColor.GREEN + Strings.repeat(String.valueOf(symbol), progressBars);
        }
        return ChatColor.GREEN + Strings.repeat(String.valueOf(symbol), progressBars) + ChatColor.RED + Strings.repeat(String.valueOf(symbol), totalBars - progressBars);
    }

    public static String getPercentage(double current, double requirement){
        return String.format("%.2f", (current / requirement) * 100) + "%";
    }

    public static boolean isInside(@NonNull Location loc, @NonNull Location l1, @NonNull Location l2) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2 && loc.getWorld().getName().equalsIgnoreCase(l1.getWorld().getName());
    }

    public static String formatPlaytime(long playtimeInSeconds){
        playtimeInSeconds = playtimeInSeconds / 1000;
        int days = (int) TimeUnit.SECONDS.toDays(playtimeInSeconds);
        long hours = TimeUnit.SECONDS.toHours(playtimeInSeconds) - (days * 24L);
        long minutes = TimeUnit.SECONDS.toMinutes(playtimeInSeconds) - (TimeUnit.SECONDS.toHours(playtimeInSeconds)* 60);
        long seconds = TimeUnit.SECONDS.toSeconds(playtimeInSeconds) - (TimeUnit.SECONDS.toMinutes(playtimeInSeconds) *60);

        return (days != 0 ? days + " days " : "") + (hours != 0 ? hours + " hours " : "") + (minutes != 0 ? minutes + " minutes " : "") + (seconds != 0 ? seconds + " seconds" : "");
    }

    public static String formatPlaytimeShort(long playtimeInSeconds){
        playtimeInSeconds = playtimeInSeconds / 1000;
        int days = (int) TimeUnit.SECONDS.toDays(playtimeInSeconds);
        long hours = TimeUnit.SECONDS.toHours(playtimeInSeconds) - (days * 24L);
        long minutes = TimeUnit.SECONDS.toMinutes(playtimeInSeconds) - (TimeUnit.SECONDS.toHours(playtimeInSeconds)* 60);
        long seconds = TimeUnit.SECONDS.toSeconds(playtimeInSeconds) - (TimeUnit.SECONDS.toMinutes(playtimeInSeconds) *60);

        return (days != 0 ? days + "d" : "") + (hours != 0 ? hours + "h" : "") + (minutes != 0 ? minutes + "m" : "") + (seconds != 0 ? seconds + "s" : "");
    }
    public static int getEmptySlots(Inventory inventory) {
        int i = inventory.getSize();
        for (ItemStack is : inventory.getContents()) {
            if(is != null && is.getType() != Material.AIR){
                i--;
            }
        }
        return i;
    }

    public static String getRandomHexColor(){
        int nextInt = ThreadLocalRandom.current().nextInt(0xffffff + 1);

        // format it as hexadecimal string (with hashtag and leading zeros)
        return String.format("#%06x", nextInt);
    }

    public static char getRandomChatColor(){
        String options = "abcdef234569";

        // format it as hexadecimal string (with hashtag and leading zeros)
        return options.charAt(ThreadLocalRandom.current().nextInt(options.length()));
    }

    public static Enchantment getEnchantmentFromName(String name){
        switch(name.toLowerCase(Locale.ROOT)){
            case "power", "pwr", "arrow_damage" -> {
                return Enchantment.ARROW_DAMAGE;
            }
            case "flame", "firearrow", "arrow_fire", "arrowfire" -> {
                return Enchantment.ARROW_FIRE;
            }
            case "infinity", "infinite", "arrow_infinite", "arrowinfinite" -> {
                return Enchantment.ARROW_INFINITE;
            }
            case "punch", "arrow_knockback" -> {
                return Enchantment.ARROW_KNOCKBACK;
            }
            case "binding", "binding_curse", "bindingcurse" -> {
                return Enchantment.BINDING_CURSE;
            }
            case "channeling", "channel" -> {
                return Enchantment.CHANNELING;
            }
            case "damage_all", "sharpness", "sharp" -> {
                return Enchantment.DAMAGE_ALL;
            }
            case "damage_arthropods", "arthropods", "arthro" -> {
                return Enchantment.DAMAGE_ARTHROPODS;
            }
            case "damage_undead", "undead" -> {
                return Enchantment.DAMAGE_UNDEAD;
            }
            case "depth_strider", "depthstrider" -> {
                return Enchantment.DEPTH_STRIDER;
            }
            case "dig_speed", "efficiency", "digspeed", "eff" -> {
                return Enchantment.DIG_SPEED;
            }
            case "durability", "unbr", "unbreak", "unbreaking" -> {
                return Enchantment.DURABILITY;
            }
            case "fire_aspect", "fireaspect", "fire" -> {
                return Enchantment.FIRE_ASPECT;
            }
            case "frost", "frostwalker", "frost_walker" -> {
                return Enchantment.FROST_WALKER;
            }
            case "impaling" -> {
                return Enchantment.IMPALING;
            }
            case "knockback" -> {
                return Enchantment.KNOCKBACK;
            }
            case "loot_bonus_blocks", "fortune" -> {
                return Enchantment.LOOT_BONUS_BLOCKS;
            }
            case "loot_bonus_mobs", "looting", "loot" -> {
                return Enchantment.LOOT_BONUS_MOBS;
            }
            case "loyalty" -> {
                return Enchantment.LOYALTY;
            }
            case "luck" -> {
                return Enchantment.LUCK;
            }
            case "lure" -> {
                return Enchantment.LURE;
            }
            case "mending" -> {
                return Enchantment.MENDING;
            }
            case "multishot" -> {
                return Enchantment.MULTISHOT;
            }
            case "oxygen", "resp", "respiration" -> {
                return Enchantment.OXYGEN;
            }
            case "piercing" -> {
                return Enchantment.PIERCING;
            }
            case "protection", "prot" -> {
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            }
            case "blastprotection", "blastprot", "protection_explosions" -> {
                return Enchantment.PROTECTION_EXPLOSIONS;
            }
            case "featherfalling", "fallprot","protection_fall" -> {
                return Enchantment.PROTECTION_FALL;
            }
            case "projectileprotection", "projectileprot", "protection_projectile" -> {
                return Enchantment.PROTECTION_PROJECTILE;
            }
            case "fireprot", "fireprotection", "protection_fire" -> {
                return Enchantment.PROTECTION_FIRE;
            }
            case "quick_charge", "quickcharge" -> {
                return Enchantment.QUICK_CHARGE;
            }
            case "riptide" -> {
                return Enchantment.RIPTIDE;
            }
            case "silk_touch", "silktouch" -> {
                return Enchantment.SILK_TOUCH;
            }
            case "soul_speed", "soulspeed" -> {
                return Enchantment.SOUL_SPEED;
            }
            case "sweeping_edge", "sweepingedge" -> {
                return Enchantment.SWEEPING_EDGE;
            }
            case "swift_sneak", "swiftsneak" -> {
                return Enchantment.SWIFT_SNEAK;
            }
            case "thorns" -> {
                return Enchantment.THORNS;
            }
            case "vanishing_curse", "vanishingcurse" -> {
                return Enchantment.VANISHING_CURSE;
            }
            case "water_worker", "waterworker" -> {
                return Enchantment.WATER_WORKER;
            }
        }
        return null;
    }


    public static <T> Collection<List<T>> partitionBasedOnSize(List<T> inputList, int size) {
        final AtomicInteger counter = new AtomicInteger(0);
        return inputList.stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement()/size))
                .values();
    }

//	public static void setBlockSuperFast(Block b, int typeId, byte data) {
//		Chunk c = b.getChunk();
//		net.minecraft.server.level.WorldServer nmsWorld = ((CraftWorld) world).getHandle();
//		net.minecraft.world.level.chunk.Chunk nmsChunk = nmsWorld.d(x >> 4, z >> 4);
//
//
//		try {
//			Field f = c.getClass().getDeclaredField("sections");
//			f.setAccessible(true);
//			ChunkSection[] sections = (ChunkSection[]) f.get(c);
//			ChunkSection chunksection = sections[b.getY() >> 4];
//
//			if (chunksection == null) {
//				if (typeId == 0)
//					return;
//				chunksection = sections[b.getY() >> 4] = new ChunkSection(b.getY() >> 4 << 4, !nmsChunk..worldProvider.f);
//			}
//
//			chunksection.a(b.getX() & 15, b.getY() & 15, b.getZ() & 15, typeId);
//			chunksection.b(b.getX() & 15, b.getY() & 15, b.getZ() & 15, data);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

    public static String capitalize(String s){
        return WordUtils.capitalize(s.replaceAll("_", " "));
    }


    public static void callEvent(Event event){
        Bukkit.getPluginManager().callEvent(event);
    }

    public static ItemStack getSkull(String url, int amount, boolean nostack){
        ItemStack is = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta is_meta = (SkullMeta) is.getItemMeta();
        GameProfile profile;
        if(nostack) {
            profile = new GameProfile(UUID.randomUUID(), null);
        }else{
            profile = new GameProfile(UUID.fromString("d884c8de-0ac0-11ed-861d-0242ac120002"), null);
        }

        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try{
            profileField = is_meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(is_meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){
            e.printStackTrace();
        }
        is.setItemMeta(is_meta);
        return is;
    }

    public static String formatCurrency(double amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    public static Color getColor(String color){
        switch(color){
            case "RED" -> {
                return Color.RED;
            }
            case "BLUE" ->{
                return Color.BLUE;
            }
            case "PURPLE" -> {
                return Color.PURPLE;
            }
            case "LIGHT_BLUE" ->{
                return Color.AQUA;
            }
            case "YELLOW" ->{
                return Color.YELLOW;
            }
            case "ORANGE" ->{
                return Color.ORANGE;
            }
            default -> {
                return Color.GREEN;
            }
        }
    }

    public static void setWorldBorder(World world, int warningDistance, double size, double centerx, double centerz){
        WorldBorder border = world.getWorldBorder();
        border.setWarningDistance(warningDistance);
        border.setSize(size + 1);
        border.setCenter(centerx, centerz);
    }

}
