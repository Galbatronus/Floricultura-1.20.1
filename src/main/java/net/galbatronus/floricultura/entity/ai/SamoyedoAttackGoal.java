package net.galbatronus.floricultura.entity.ai;

import net.galbatronus.floricultura.entity.custom.SamoyedoEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SamoyedoAttackGoal extends MeleeAttackGoal {
    private final SamoyedoEntity entity;
    private int attackDelay = 30; // Cuánto tiempo tarda en prepararse el ataque
    private int ticksUntilNextAttack = 40; // Contador para saber cuándo puede volver a atacar
    private boolean shouldCountTillNextAttack = false; // Si debe empezar a contar para el siguiente ataque

    // Constructor: se pasa la entidad, velocidad de ataque y si sigue al objetivo aunque no lo vea
    public SamoyedoAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((SamoyedoEntity) pMob); // Se castea a SamoyedoEntity porque se asume que solo aplica a él
    }

    // Se llama cuando se inicia el objetivo de ataque
    @Override
    public void start() {
        super.start();
        attackDelay = 30; // Reinicia el tiempo entre ataques
        ticksUntilNextAttack = 40;
    }

    // Se llama continuamente para revisar si puede atacar
    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        // Si el enemigo está dentro del rango de ataque
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            // Si ya es momento de empezar la animación
            if(isTimeToStartAttackAnimation()) {
                entity.setAttacking(true); // Llama al metodo que activa la animación
            }

            // Si ya es tiempo de atacar realmente
            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy); // Realiza el ataque
            }
        } else {
            // Si el enemigo está muy lejos, reinicia todo
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            entity.attackAnimationTimeout = 0; // Reinicia el temporizador de animación (si lo usas)
        }
    }

    // Revisa si el enemigo está en distancia de ataque
    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    // Reinicia el contador para el proximo ataque
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    // Devuelve true si ya puede atacar
    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    // Devuelve true si ya puede iniciar la animación de ataque
    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    // Devuelve cuántos ticks faltan para el próximo ataque
    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    // Ejecuta el ataque al enemigo
    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown(); // Reinicia cooldown
        this.mob.swing(InteractionHand.MAIN_HAND); // Hace la animación de golpear
        if (!this.mob.level().isClientSide()) {
            this.mob.doHurtTarget(pEnemy); // Daña al enemigo
        }
    }

    // Tick de IA: decrementa el tiempo si corresponde
    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    // Se llama cuando la IA deja de ejecutarse
    @Override
    public void stop() {
        entity.setAttacking(false); // Detiene animación personalizada
        super.stop();
    }
}