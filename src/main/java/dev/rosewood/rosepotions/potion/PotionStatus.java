package dev.rosewood.rosepotions.potion;

public class PotionStatus {

    private final String name;
    private long activationTime;
    private long duration;
    private int amplifier;

    /**
     * Represents the status of a potion effect
     *
     * @param name           The name of the potion effect
     * @param activationTime The time the potion effect was activated (in millis)
     * @param duration       The duration of the potion effect (in millis)
     * @param amplifier      The amplifier of the potion effect
     */
    public PotionStatus(String name, long activationTime, long duration, int amplifier) {
        this.name = name;
        this.activationTime = activationTime;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    /**
     * Checks if the potion effect has expired
     *
     * @return True if the potion effect has expired
     */
    public boolean isExpired() {
        if (this.activationTime == -1) {
            this.activationTime = System.currentTimeMillis();
            return false;
        }

        return System.currentTimeMillis() >= this.activationTime + this.duration;
    }

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public long getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(long activationTime) {
        this.activationTime = activationTime;
    }

}
