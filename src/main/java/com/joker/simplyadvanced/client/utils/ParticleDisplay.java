package com.joker.simplyadvanced.client.utils;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleDisplay {
    private World world;
    private int count = 1;
    private double addX = 0.0D, addY = 0.0D, addZ = 0.0D, speedX = 0.0D, speedY = 0.0D, speedZ = 0.0D, spreadX = 0.0D, spreadY = 0.0D, spreadZ = 0.0D;
    private BlockPos pos;
    private EnumParticleTypes particle;

    public ParticleDisplay (World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public ParticleDisplay setParticleType (EnumParticleTypes particle) {
        this.particle = particle;
        return this;
    }

    public ParticleDisplay setCount(int count) {
        this.count = count;
        return this;
    }

    public ParticleDisplay setSpread (double spreadX, double spreadY, double spreadZ) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        return this;
    }

    public void spawnParticles(boolean center) {
        double x = pos.getX() + (center?0.5:0.0);
        double y = pos.getY() + (center?0.5:0.0);
        double z = pos.getZ() + (center?0.5:0.0);

        for (int i = 0; i < count; ++i) {
            double d1 = (x+addX) + spreadX;
            double d2 = (y+addY) + spreadY;
            double d3 = (z+addZ) + spreadZ;

            world.spawnParticle(particle, d1, d2, d3, speedX, speedY, speedZ);
        }
    }
}
