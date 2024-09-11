package me.deepdive.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * This class represents a serializable Location for Minecraft.
 * It only stores what's absolutely necessary, and has methods to convert back into Location.
 */
@Getter
@Setter
public class SimpleLocation {

    private double x;
    private double y;
    private double z;
    private String world;
    private float pitch;
    private float yaw;


    public SimpleLocation(Location loc){
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.world = loc.getWorld().getName();
        this.pitch = loc.getPitch();
        this.yaw = loc.getYaw();
    }

    public SimpleLocation(double x, double y, double z, String world, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public SimpleLocation(double x, double y, double z, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    public Location toLocationFull(){
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
    public Location toLocation(){
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Block getBlock(){
        return toLocation().getBlock();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SimpleLocation)) {
            return false;
        } else {
            SimpleLocation other = (SimpleLocation)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (Double.compare(this.getX(), other.getX()) != 0) {
                return false;
            } else if (Double.compare(this.getY(), other.getY()) != 0) {
                return false;
            } else if (Double.compare(this.getZ(), other.getZ()) != 0) {
                return false;
            } else if (Float.compare(this.getYaw(), other.getYaw()) != 0) {
                return false;
            } else if (Float.compare(this.getPitch(), other.getPitch()) != 0) {
                return false;
            } else {
                Object this$world = this.getWorld();
                Object other$world = other.getWorld();
                if (this$world == null) {
                    if (other$world != null) {
                        return false;
                    }
                } else if (!this$world.equals(other$world)) {
                    return false;
                }

                return true;
            }
        }
    }
    protected boolean canEqual(Object other) {
        return other instanceof SimpleLocation;
    }

    public String toString() {
        return this.getWorld() + "," + this.getX() + "," + this.getY() + "," + this.getZ() + "," + this.getYaw() + "," + this.getPitch();
    }

    public static SimpleLocation fromString(String serialized){
        String[] split = serialized.split(",");
        return new SimpleLocation(Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                split[0],
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }

}
