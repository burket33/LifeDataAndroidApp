package com.timburkepe.lifedata;

public class SleepModel {

    private long bedtime;
    private long wakeupTime;
    private String quality;
    private String description;

    public SleepModel() {
    }

    public SleepModel(long bedtime, long wakeupTime,
                      String quality, String description) {
        this.bedtime = bedtime;
        this.wakeupTime = wakeupTime;
        this.quality = quality;
        this.description = description;
    }

    public long getBedtime() {
        return bedtime;
    }

    public void setBedtime(long bedtime) {
        this.bedtime = bedtime;
    }

    public long getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(long wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
