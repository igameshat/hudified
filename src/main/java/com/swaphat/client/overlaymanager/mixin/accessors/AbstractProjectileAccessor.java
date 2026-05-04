package com.swaphat.client.overlaymanager.mixin.accessors;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public interface AbstractProjectileAccessor {

    // @Invoker allows us to call a protected method from outside the class!
    @Invoker("isInGround")
    boolean overlayManager$isInGround();
}