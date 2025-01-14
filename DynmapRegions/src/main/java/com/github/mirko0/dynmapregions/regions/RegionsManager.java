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
import org.bukkit.scheduler.BukkitTask;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

import java.util.*;
import java.util.stream.Collectors;

public class RegionsManager {

    private final BotSettings settings;
    @Getter
    private MarkerSet markerSet;


    public RegionsManager(AddonMain addonMain) {
        this.settings = addonMain.getSettings();
        String layerName = settings.getLayerName();

        DynmapAPI dynmapAPI = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if (dynmapAPI == null) {
            AddonMain.log("Unable to retrieve dynmapAPI. Addon shutting down.");
            return;
        }
        MarkerAPI markerAPI = dynmapAPI.getMarkerAPI();
        markerSet = markerAPI.getMarkerSet("ultraregions.markerset");
        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet("ultraregions.markerset", layerName, null, false);
        }else {
            markerSet.setMarkerSetLabel(layerName);
        }
        markerSet.setLayerPriority(10);
        markerSet.setHideByDefault(false);
        start();
    }

    private BukkitTask updateTask;

    public void start() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimerAsynchronously(UltraRegions.getInstance().getBootstrap(), 20L, settings.getRefreshTimeInMinutes());
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        AddonMain.log("Shutting Down Update Task");
        if (updateTask != null && !updateTask.isCancelled()) updateTask.cancel();
    }

    public void update() {
        HashMap<World, Region[]> regions = UltraRegions.getAPI().getWorlds().get().stream().collect(Collectors.toMap(ManagedWorld::getBukkitWorld, ManagedWorld::getRegions, (a, b) -> b, HashMap::new));
        if (!regions.isEmpty()) {
            for (Map.Entry<World, Region[]> r : regions.entrySet()) {
                for (Region region : r.getValue()) {
                    if (region == null) continue;
                    if (region.getName() == null) continue;
                    if (region.getName().equals("Global")) continue;
                    for (Object selection : region.getSelectionList().getList()) {
                        if (selection instanceof CuboidSelection found) {
                            // CUBE REGIONS
                            cuboidMarker(r.getKey(), region, found);
                        }else if (selection instanceof SphereSelection found) {
                            // SPHERE REGIONS
                            if (settings.isUse3D())
                                sphereMarker(r.getKey(), region, found);
                            else circleMarker(r.getKey(), region, found);
                        }else if (selection instanceof ExpandVertSelection found) {
                            // VERTICAL REGIONS
                            cuboidMarker(r.getKey(), region, found);
                        }
                    }
                }
            }
        }
    }

    public String regionId(Region region, Selection selection) {
        XYZ center = selection.getCenter();
        return "Region_" + region.getUuid() + "_" + center.getX() + center.getY() + center.getZ() + selection.getBlockSize();
    }

    public void cuboidMarker(World world, Region region, Selection selection) {
        if (region.getName().equals("Global")) return;
        final String markerId = regionId(region, selection);
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

        AreaMarker found = markerSet.findAreaMarker(markerId);
        AreaMarker marker = found != null ? found : markerSet.createAreaMarker(markerId, region.getName(), false, worldName, x, z, false);
        if (marker == null) {
            AddonMain.log("Marker: " + markerId + " - was null?");
            return;
        }
        marker.setCornerLocations(x, z);
        marker.setLabel(region.getName());

        if (settings.isUse3D()) marker.setRangeY(higherBounds.getY() + 1.0, lowerBounds.getY());
        cuboidStyle(marker);
        marker.setDescription(infoWindow(region, selection));
    }

    public void circleMarker(World world, Region region, SphereSelection selection) {
        if (region.getName().equals("Global")) return;
        final String markerId = regionId(region, selection);
        final String worldName = world.getName();
        CircleMarker found = markerSet.findCircleMarker(markerId);
        CircleMarker marker = found != null ? found : markerSet.createCircleMarker(markerId, region.getName(), false, worldName, selection.getCenter().getX(), selection.getCenter().getY(), selection.getCenter().getZ(), selection.getRadius() + 1.0, selection.getRadius() + 1.0, false);
        if (marker == null) {
            AddonMain.log("Marker: " + markerId + " - was null?");
            return;
        }
        marker.setRadius(selection.getRadius() + 1.0, selection.getRadius() + 1.0);
        marker.setCenter(worldName, selection.getCenter().getX(), selection.getCenter().getY(), selection.getCenter().getZ());
        marker.setLabel(region.getName());
        circleStyle(marker);

        marker.setDescription(infoWindow(region, selection));
    }

    public void sphereMarker(World world, Region region, SphereSelection selection) {
        if (region.getName().equals("Global")) return;
        final String markerId = regionId(region, selection);
        final String worldName = world.getName();
        final double radius = selection.getRadius();
        final double centerX = selection.getCenter().getX();
        final double centerY = selection.getCenter().getY();
        final double centerZ = selection.getCenter().getZ();
        CornerLocations calculation = calculateLocations(centerX, centerY, centerZ, radius);
        PolyLineMarker found = markerSet.findPolyLineMarker(markerId);
        PolyLineMarker marker = found != null ? found : markerSet.createPolyLineMarker(markerId, region.getName(), false, worldName, calculation.x, calculation.y, calculation.z, false);
        if (marker == null) {
            AddonMain.log("Marker: " + markerId + " - was null?");
            return;
        }

        marker.setLabel(region.getName());
        // Apply styling and description
        polyLineStyle(marker);
        marker.setDescription(infoWindow(region, selection));
    }

    private record CornerLocations(double[] x, double[] y, double[] z) {
    }

    private CornerLocations calculateLocations(double centerX, double centerY, double centerZ, double radius) {
        final int latSegments = 23; // Number of horizontal slices (latitude)
        final int lonSegments = 30; // Number of vertical slices (longitude)
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


    private void polyLineStyle(PolyLineMarker marker) {
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

    private void circleStyle(CircleMarker marker) {
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

    private void cuboidStyle(AreaMarker marker) {
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

    private String infoWindow(Region region, Selection selection) {
        String html = """
                <div class="regioninfo">
                    <center>
                        <div class="infowindow">
                            <span style="font-weight:bold;"> {regionName} </span><br/>
                            {info}
                        </div>
                    </center>
                </div>
                """;

        html = html.replace("{regionName}", region.getName());

        StringBuilder info = new StringBuilder();
        info.append("Blocks: ").append(selection.getBlockSize()).append("<br/>");
        XYZ center = selection.getCenter();
        info.append("Center: ").append(center.getX()).append("x ").append(center.getY()).append("y ").append(center.getZ()).append("z<br/>");
        if (selection instanceof SphereSelection sec) {
            info.append("Radius: ").append(sec.getRadius());
        }
        if (selection instanceof CuboidSelection sec) {
            XYZ a = sec.getA();
            XYZ b = sec.getB();
            info.append("XYZ 1: ").append(a.getX()).append("x ").append(a.getY()).append("y ").append(a.getZ()).append("z<br/>");
            info.append("XYZ 2: ").append(b.getX()).append("x ").append(b.getY()).append("y ").append(b.getZ()).append("z");
        }
        if (selection instanceof ExpandVertSelection sec) {
            XYZ a = sec.getA();
            XYZ b = sec.getB();
            info.append("XYZ 1: ").append(a.getX()).append("x ").append(a.getY()).append("y ").append(a.getZ()).append("z<br/>");
            info.append("XYZ 2: ").append(b.getX()).append("x ").append(b.getY()).append("y ").append(b.getZ()).append("z");
        }
        html = html.replace("{info}", info);
        return html;
    }
}