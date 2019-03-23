package metier.service;

import com.google.maps.model.LatLng;
import util.GeoTest;

public class ServiceGeo {
    public ServiceGeo() {}

    public LatLng calculCoord(String s)
    {
        return GeoTest.getLatLng(s);
    }

    public double toRad(double angleInDegree) {
        return angleInDegree * Math.PI / 180.0;
    }

    public double getFlightDistanceInKm(LatLng origin, LatLng destination) {

        // From: http://www.movable-type.co.uk/scripts/latlong.html
        double R = 6371.0; // Average radius of Earth (km)
        double dLat = toRad(destination.lat - origin.lat);
        double dLon = toRad(destination.lng - origin.lng);
        double lat1 = toRad(origin.lat);
        double lat2 = toRad(destination.lat);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        double d = R * c;

        return Math.round(d * 1000.0) / 1000.0;
    }
}
