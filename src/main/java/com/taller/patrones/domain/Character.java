package com.taller.patrones.domain;

/**
 * Representa un personaje en combate.
 */
public class Character {

    private final String name;
    private int currentHp;
    private final int maxHp;
    private final int attack;
    private final int defense;
    private final int speed;

    // campos adicionales opcionales (ejemplos)
    private final String charClass;
    private final String equipment;
    private final String buffs;

    // constructor privado usado por el builder
    private Character(Builder builder) {
        this.name = builder.name;
        this.maxHp = builder.maxHp;
        this.currentHp = builder.maxHp;
        this.attack = builder.attack;
        this.defense = builder.defense;
        this.speed = builder.speed;
        this.charClass = builder.charClass;
        this.equipment = builder.equipment;
        this.buffs = builder.buffs;
    }

    /**
     * Constructor antiguo conservado para compatibilidad rápida.
     * Se recomienda usar {@link Builder} en su lugar.
     */
    public Character(String name, int maxHp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.charClass = null;
        this.equipment = null;
        this.buffs = null;
    }

    public String getName() { return name; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    public String getCharClass() { return charClass; }
    public String getEquipment() { return equipment; }
    public String getBuffs() { return buffs; }

    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, currentHp - damage);
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    /**
     * Builder para construir personajes con muchos atributos opcionales.
     */
    public static class Builder {
        private final String name;
        private int maxHp = 100;
        private int attack = 10;
        private int defense = 5;
        private int speed = 5;
        private String charClass;
        private String equipment;
        private String buffs;

        public Builder(String name) {
            this.name = name;
        }

        public Builder maxHp(int maxHp) { this.maxHp = maxHp; return this; }
        public Builder attack(int attack) { this.attack = attack; return this; }
        public Builder defense(int defense) { this.defense = defense; return this; }
        public Builder speed(int speed) { this.speed = speed; return this; }
        public Builder charClass(String charClass) { this.charClass = charClass; return this; }
        public Builder equipment(String equipment) { this.equipment = equipment; return this; }
        public Builder buffs(String buffs) { this.buffs = buffs; return this; }

        public Character build() {
            return new Character(this);
        }
    }

    public double getHpPercentage() {
        return maxHp > 0 ? (double) currentHp / maxHp * 100 : 0;
    }
}
