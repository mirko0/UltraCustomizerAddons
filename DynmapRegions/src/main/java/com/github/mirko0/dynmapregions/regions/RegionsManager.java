package com.github.mirko0.dynmapregions.regions;


import com.github.mirko0.dynmapregions.AddonMain;
import com.github.mirko0.dynmapregions.BotSettings;
import com.github.mirko0.dynmapregions.ColorUtil;
import lombok.Getter;
import me.TechsCode.UltraRegions.UltraRegions;
import me.TechsCode.UltraRegions.selection.*;
import me.TechsCode.UltraRegions.storage.ManagedWorld;
import me.TechsCode.UltraRegions.storage.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegionsManager {

    private final AddonMain instance;
    private final BotSettings settings;
    private boolean stop;

    @Getter
    private MarkerSet markerSet;

    @Getter
    private Map<String, AreaMarker> allRegionsCuboid = new HashMap<>();
    @Getter
    private Map<String, CircleMarker> allRegionsSphere = new HashMap<>();
    @Getter
    private Map<String, PolyLineMarker> allRegionsPolyline = new HashMap<>();

    public RegionsManager(AddonMain addonMain) {
        this.instance = addonMain;
        this.settings = addonMain.getSettings();
        setupMarkerSet();
    }

    public void setupMarkerSet() {
        DynmapAPI dynmapAPI = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");

        markerSet = dynmapAPI.getMarkerAPI().getMarkerSet("ultraregions.markerset");

        final String layerName = settings.getLayerName();
        if (markerSet == null) {
            markerSet = dynmapAPI.getMarkerAPI().createMarkerSet("ultraregions.markerset", layerName, null, false);
        }else {
            markerSet.setMarkerSetLabel(layerName);
        }

        markerSet.setLayerPriority(10);
        markerSet.setHideByDefault(false);
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (stop) {
                    stop = false;
                    this.cancel();
                }
                update();
            }
        }.runTaskTimer(UltraRegions.getInstance().getBootstrap(), 20L, settings.getRefreshTimeInMinutes());
    }

    public void stop() {
        this.stop = true;
    }

    public void stopThenStart() {
        stop();
        start();
    }

    public void update() {
        Map<String, AreaMarker> newRegionsCuboid = new HashMap<>();
        Map<String, CircleMarker> newRegionsSphere = new HashMap<>();
        Map<String, PolyLineMarker> newRegionsPolyline = new HashMap<>();

        HashMap<World, Region[]> regions = UltraRegions.getAPI().getWorlds().get().stream().collect(Collectors.toMap(ManagedWorld::getBukkitWorld, ManagedWorld::getRegions, (a, b) -> b, HashMap::new));

        if (!regions.isEmpty()) {
            for (Map.Entry<World, Region[]> r : regions.entrySet()) {
                for (Region region : r.getValue()) {
                    if (region == null) continue;
                    if (region.getName() == null) continue;
                    if (region.getName().equals("Global")) continue;
                    for (Object selection : region.getSelectionList().getList()) {
                        if (selection instanceof CuboidSelection found) {
                            createRegionMarkerCuboid(r.getKey(), region, newRegionsCuboid, found);
                        }else if (selection instanceof SphereSelection found) {
                            if (settings.isUse3D()) createRegionMarkerSphere(r.getKey(), region, newRegionsPolyline, found);
                            else createRegionMarkerCircle(r.getKey(), region, newRegionsSphere, found);
                        }else if (selection instanceof ExpandVertSelection found) {
                            createRegionMarkerCuboid(r.getKey(), region, newRegionsCuboid, found);
                        }
                    }
                }
            }
        }

        allRegionsCuboid.values().forEach(GenericMarker::deleteMarker);
        allRegionsSphere.values().forEach(GenericMarker::deleteMarker);
        allRegionsPolyline.values().forEach(GenericMarker::deleteMarker);

        allRegionsCuboid.clear();
        allRegionsCuboid = newRegionsCuboid;
        allRegionsSphere.clear();
        allRegionsSphere = newRegionsSphere;
        allRegionsPolyline.clear();
        allRegionsPolyline = newRegionsPolyline;
    }

    public void createRegionMarkerCuboid(World world, Region region, Map<String, AreaMarker> regionsMap, Selection selection) {
        if (region.getName().equals("Global")) return;

        final String markerId = "Region_" + region.getUuid();
        final String worldName = world.getName();
        XYZ a = null;
        XYZ b = null;
        if (selection instanceof CuboidSelection cuboidSelection) {
            a = cuboidSelection.getA();
            b = cuboidSelection.getB();
        }else if (selection instanceof ExpandVertSelection expandVertSelection) {
            a = expandVertSelection.getA();
            b = expandVertSelection.getB();
        }
        if (a == null || b == null) return;

        Location lowerBounds = a.getLocationInWorld(world);
        Location higherBounds = b.getLocationInWorld(world);
        if (lowerBounds == null | higherBounds == null) return;

        double[] x = new double[4];
        double[] z = new double[4];
        x[0] = lowerBounds.getX();
        z[0] = lowerBounds.getZ();
        x[1] = lowerBounds.getX();
        z[1] = higherBounds.getZ() + 1.0;
        x[2] = higherBounds.getX() + 1.0;
        z[2] = higherBounds.getZ() + 1.0;
        x[3] = higherBounds.getX() + 1.0;
        z[3] = lowerBounds.getZ();

        AreaMarker marker = allRegionsCuboid.remove(markerId);
        if (marker == null) {
            marker = markerSet.createAreaMarker(markerId, region.getName(), false, worldName, x, z, false);
            if (marker == null) return;
        }else {
            marker.setCornerLocations(x, z);
            marker.setLabel(region.getName());
        }
        if (settings.isUse3D()) marker.setRangeY(higherBounds.getY() + 1.0, lowerBounds.getY());

        setMarkerCuboidStyle(marker);

        marker.setDescription(formatInfoWindow(region, selection));

        regionsMap.put(markerId, marker);
    }

    public void createRegionMarkerCircle(World world, Region region, Map<String, CircleMarker> regionsMap, SphereSelection selection) {
        if (region.getName().equals("Global")) return;

        final String markerId = "Region_" + region.getUuid();
        final String worldName = world.getName();
        CircleMarker marker = allRegionsSphere.remove(markerId);
        if (marker == null) {
            marker = markerSet.createCircleMarker(
                    markerId,
                    region.getName(),
                    false,
                    worldName,
                    selection.getCenter().getX(),
                    selection.getCenter().getY(),
                    selection.getCenter().getZ(),
                    selection.getRadius() + 1.0,
                    selection.getRadius() + 1.0,
                    false);
            if (marker == null) return;
        }else {
            marker.setRadius(selection.getRadius() + 1.0, selection.getRadius() + 1.0);
            marker.setCenter(worldName, selection.getCenter().getX(), selection.getCenter().getY(), selection.getCenter().getZ());
            marker.setLabel(region.getName());
        }
        setMarkerCircleStyle(marker);

        marker.setDescription(formatInfoWindow(region, selection));

        regionsMap.put(markerId, marker);
    }

    public void createRegionMarkerSphere(World world, Region region, Map<String, PolyLineMarker> regionsMap, SphereSelection selection) {
        if (region.getName().equals("Global")) return;

        final String markerId = "Region_" + region.getUuid();
        final String worldName = world.getName();
        final int latSegments = 23; // Number of horizontal slices (latitude)
        final int lonSegments = 30; // Number of vertical slices (longitude)
        final double radius = selection.getRadius();
        final double centerX = selection.getCenter().getX();
        final double centerY = selection.getCenter().getY();
        final double centerZ = selection.getCenter().getZ();
        CornerLocations calculation = calculateLocations(latSegments, lonSegments, centerX, centerY, centerZ, radius);
        PolyLineMarker marker = regionsMap.remove(markerId);
        if (marker == null) {
            marker = markerSet.createPolyLineMarker(markerId, region.getName(), false, worldName, calculation.x, calculation.y, calculation.z, false);
            if (marker == null) return;
        }else {
            marker.setCornerLocations(calculation.x, calculation.y, calculation.z);
            marker.setLabel(region.getName());
        }
        // Apply styling and description
        setMarkerLineStyle(marker);
        marker.setDescription(formatInfoWindow(region, selection));
        regionsMap.put(markerId, marker);
    }


    private record CornerLocations(double[] x, double[] y, double[] z) {
    }

    private CornerLocations calculateLocations(int latSegments, int lonSegments, double centerX, double centerY, double centerZ, double radius) {
        // Lists to hold combined coordinates for all lines
        List<Double> allX = new ArrayList<>();
        List<Double> allY = new ArrayList<>();
        List<Double> allZ = new ArrayList<>();

        // Generate latitude (horizontal) circles
        for (int i = 0; i <= latSegments; i++) {
            double phi = Math.PI * i / latSegments; // Latitude angle from 0 to π (north to south)
            double latY = centerY + radius * Math.cos(phi); // Height of the circle
            double latRadius = radius * Math.sin(phi); // Radius of the circle at this height

            for (int j = 0; j <= lonSegments; j++) {
                double theta = 2.0 * Math.PI * j / lonSegments; // Full circle (0 to 2π)
                allX.add(centerX + latRadius * Math.cos(theta));
                allY.add(latY);
                allZ.add(centerZ + latRadius * Math.sin(theta));
            }
        }

        // Generate longitude (vertical) circles
        for (int i = 0; i <= lonSegments; i++) {
            double theta = 2.0 * Math.PI * i / lonSegments; // Longitude angle around the equator
            for (int j = 0; j <= latSegments; j++) {
                double phi = Math.PI * j / latSegments; // Latitude angle from 0 to π (north to south)
                allX.add(centerX + radius * Math.sin(phi) * Math.cos(theta));
                allY.add(centerY + radius * Math.cos(phi));
                allZ.add(centerZ + radius * Math.sin(phi) * Math.sin(theta));
            }
        }

        // Convert lists to arrays and set combined coordinates
        double[] combinedX = allX.stream().mapToDouble(Double::doubleValue).toArray();
        double[] combinedY = allY.stream().mapToDouble(Double::doubleValue).toArray();
        double[] combinedZ = allZ.stream().mapToDouble(Double::doubleValue).toArray();

        return new CornerLocations(combinedX, combinedY, combinedZ);
    }


    private void setMarkerLineStyle(PolyLineMarker marker) {
        int lineColor = 0xFF0000;

        try {
            lineColor = ColorUtil.hex2RGB(settings.getLineColor()).getRGB();
        } catch (Exception ex) {
            AddonMain.log("&cInvalid style color specified. Defaulting to red!");
        }

        int lineWeight = settings.getWeight();
        double lineOpacity = settings.getLineOpacity();

        marker.setLineStyle(lineWeight, lineOpacity, lineColor);
    }

    private void setMarkerCircleStyle(CircleMarker marker) {
        int lineColor = 0xFF0000;
        int fillColor = 0xFF0000;

        try {
            lineColor = ColorUtil.hex2RGB(settings.getLineColor()).getRGB();
            fillColor = ColorUtil.hex2RGB(settings.getFillColor()).getRGB();
        } catch (Exception ex) {
            AddonMain.log("&cInvalid style color specified. Defaulting to red!");
        }

        int lineWeight = settings.getWeight();
        double lineOpacity = settings.getLineOpacity();
        double fillOpacity = settings.getFillOpacity();

        marker.setLineStyle(lineWeight, lineOpacity, lineColor);
        marker.setFillStyle(fillOpacity, fillColor);
    }

    private void setMarkerCuboidStyle(AreaMarker marker) {
        int lineColor = 0xFF0000;
        int fillColor = 0xFF0000;

        try {
            lineColor = ColorUtil.hex2RGB(settings.getLineColor()).getRGB();
            fillColor = ColorUtil.hex2RGB(settings.getFillColor()).getRGB();
        } catch (Exception ex) {
            AddonMain.log("&cInvalid style color specified. Defaulting to red!");
        }

        int lineWeight = settings.getWeight();
        double lineOpacity = settings.getLineOpacity();
        double fillOpacity = settings.getFillOpacity();

        marker.setLineStyle(lineWeight, lineOpacity, lineColor);
        marker.setFillStyle(fillOpacity, fillColor);
    }

    private String formatInfoWindow(Region region, Selection selection) {
        return "<div class=\"regioninfo\">" +
                "<center>" +
                "<div class=\"infowindow\">" +
                "<span style=\"font-weight:bold;\">" + region.getName() + "</span><br/>" +
                (selection instanceof CuboidSelection || selection instanceof ExpandVertSelection ? "Blocks: " + selection.getBlockSize() : "Radius: " + ((SphereSelection) selection).getRadius()) +
                "</div>" +
                "</center>" +
                "</div>";
    }
}