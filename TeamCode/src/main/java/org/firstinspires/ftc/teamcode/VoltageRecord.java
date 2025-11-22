package org.firstinspires.ftc.teamcode;


public class VoltageRecord {
    double voltage ;
    double shortDistanceFactor;
    double longDistanceFactor;
    double autoPickSpeed;
    double autoShooterFactor;
    double autoShootCloserFactor;

    public double getAutoShootCloserFactor() {
        return autoShootCloserFactor;
    }

    public VoltageRecord(double voltage, double shortDistanceFactor, double longDistanceFactor, double autoPickSpeed, double autoShooterFactor, double autoShootCloserFactor) {
        this.voltage = voltage;
        this.shortDistanceFactor = shortDistanceFactor;
        this.longDistanceFactor = longDistanceFactor;
        this.autoPickSpeed = autoPickSpeed;
        this.autoShooterFactor = autoShooterFactor;
        this.autoShootCloserFactor = autoShootCloserFactor;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getShortDistanceFactor() {
        return shortDistanceFactor;
    }

    public double getLongDistanceFactor() {
        return longDistanceFactor;
    }

    public double getAutoPickSpeed() {
        return autoPickSpeed;
    }

    public double getAutoShooterFactor() {
        return autoShooterFactor;
    }

    @Override
    public String toString() {
        return "VoltageRecord{" +
                "voltage=" + voltage +
                ", shortDistanceFactor=" + shortDistanceFactor +
                ", longDistanceFactor=" + longDistanceFactor +
                ", autoPickSpeed=" + autoPickSpeed +
                ", autoShooterFactor=" + autoShooterFactor +
                ", autoShootCloserFactor=" + autoShootCloserFactor +
                '}';
    }
}
