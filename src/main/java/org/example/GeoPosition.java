package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeoPosition {
    private double longitude;
    private double latitude;

    public GeoPosition(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double haversineDistanceKilometersTo(GeoPosition anotherGeoPosition) {
        double longitude = anotherGeoPosition.longitude;
        double latitude = anotherGeoPosition.latitude;

        final double EARTH_RADIUS = 6371; // kilometers

        double lngDistance = Math.toRadians(longitude - this.longitude);
        double latDistance = Math.toRadians(latitude - this.latitude);

        double a = Math.pow(Math.sin(latDistance / 2), 2) + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(latitude)) * Math.pow(Math.sin(lngDistance / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return this.truncate(EARTH_RADIUS * c);
    }

    public boolean equals(GeoPosition geoPosition) {
        return this.longitude == geoPosition.longitude &&
                this.latitude == geoPosition.latitude;
    }

    private double truncate(double number) {
        BigDecimal truncated = new BigDecimal(number).setScale(2, RoundingMode.DOWN);
        return truncated.doubleValue();
    }
}
