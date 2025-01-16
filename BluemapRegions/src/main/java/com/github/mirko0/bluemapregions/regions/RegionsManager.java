package com.github.mirko0.bluemapregions.regions;


import com.github.mirko0.bluemapregions.AddonMain;
import com.github.mirko0.bluemapregions.BotSettings;
import com.github.mirko0.bluemapregions.util.ColorUtil;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.markers.ExtrudeMarker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;
import me.TechsCode.UltraRegions.UltraRegions;
import me.TechsCode.UltraRegions.selection.*;
import me.TechsCode.UltraRegions.storage.ManagedWorld;
import me.TechsCode.UltraRegions.storage.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.mirko0.bluemapregions.AddonMain.log;

public class RegionsManager {

    private final BotSettings settings;
    private static final String MARKER_SET_ID = "ultraregions";
    private static final String LABEL = "Regions";
    private final Color fillColor;
    private final Color lineColor;


    private BlueMapAPI api;

    public RegionsManager(AddonMain addonMain) {
        this.settings = addonMain.getSettings();
        java.awt.Color color = ColorUtil.hex2RGB(settings.getLineColor());
        java.awt.Color fill = ColorUtil.hex2RGB(settings.getFillColor());
        fillColor = (new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 0.3f));
        lineColor = (new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.3f));

        BlueMapAPI.getInstance().ifPresent(this::start);
        BlueMapAPI.onEnable(this::start);
        BlueMapAPI.onDisable(api -> {
            stop();
        });
    }

    private BukkitTask updateTask;

    public void start(BlueMapAPI api) {
        this.api = api;
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimerAsynchronously(UltraRegions.getInstance().getBootstrap(), 20L, settings.getRefreshTimeInMinutes());
    }

    public void stop() {
        log("Shutting Down Update Task");
        if (updateTask != null && !updateTask.isCancelled()) updateTask.cancel();
        api.getMaps().forEach(map -> map.getMarkerSets().remove(MARKER_SET_ID));
        this.api = null;
    }

    public void update() {
        HashMap<World, Region[]> regions = UltraRegions.getAPI().getWorlds().get().stream().collect(Collectors.toMap(ManagedWorld::getBukkitWorld, ManagedWorld::getRegions, (a, b) -> b, HashMap::new));
        if (regions.isEmpty()) return;

        for (Map.Entry<World, Region[]> entry : regions.entrySet()) {
            World world = entry.getKey();
            for (Region region : entry.getValue()) {
                if (region == null) continue;
                if (region.getName() == null) continue;
                if (region.getName().equals("Global")) continue;
                for (Object selection : region.getSelectionList().getList()) {
                    picker(world, region, selection);
                }

            }
        }
        if (oldIds.isEmpty()) return;
        for (IdData oldId : oldIds) {
            if (newIds.stream().anyMatch(idData -> idData.id.equals(oldId.id))) continue;
            MarkerSet markerSet = worldSets.get(oldId.worldName);
            if (markerSet == null) continue;
            markerSet.getMarkers().remove(oldId.id);
        }

    }

    private record IdData(String id, String worldName){}

    private Map<String, MarkerSet> worldSets = new HashMap<>();
    private List<IdData> oldIds = new ArrayList<>();
    private List<IdData> newIds = new ArrayList<>();

    private void picker(World world, Region region, Object selection) {
        if (!worldSets.containsKey(world.getName())) {
            final MarkerSet markerSet = MarkerSet.builder().label(LABEL).build();
            api.getWorld(world.getName())
                    .map(BlueMapWorld::getMaps)
                    .ifPresent(maps -> maps.forEach(map -> map.getMarkerSets().put(MARKER_SET_ID, markerSet)));
            worldSets.put(world.getName(), markerSet);
        }
        MarkerSet markerSet = worldSets.get(world.getName());
        if (selection instanceof CuboidSelection found) {
            cuboidMarker(world, region, found, markerSet);
            return;
        }

        if (selection instanceof ExpandVertSelection found) {
            cuboidMarker(world, region, found, markerSet);
            return;
        }

        if (selection instanceof SphereSelection found) {
            sphereMarker(world, region, found, markerSet);
        }
    }


    public String regionId(Region region, Selection selection, World world) {
        XYZ center = selection.getCenter();
        String id = "Region_" + region.getUuid() + "_" + center.getX() + center.getY() + center.getZ() + selection.getBlockSize();
        newIds.add(new IdData(id, world.getName()));
        return id;
    }

    public void cuboidMarker(World world, Region region, Selection selection, MarkerSet set) {
        if (region.getName().equals("Global")) return;
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

        Location bottom = a.getLocationInWorld(world);
        Location top = b.getLocationInWorld(world);
        if (bottom == null || top == null) return;

        float fixPos = 0.5f;
        float fixPosY = 1f;
        Shape rect = Shape.createRect(bottom.getX() - fixPos, bottom.getZ() - fixPos, top.getX() + fixPos, top.getZ() + fixPos);
        ExtrudeMarker marker = new ExtrudeMarker.Builder()
                .shape(rect, bottom.getBlockY(), top.getBlockY() + fixPosY)
                .label(region.getName())
                .lineColor(lineColor)
                .lineWidth(3)
                .depthTestEnabled(false)
                .detail(infoWindow(region, selection)).build();
        set.getMarkers().put(regionId(region, selection, world), marker);
    }

    public void sphereMarker(World world, Region region, Selection selection, MarkerSet set) {
        if (region.getName().equals("Global")) return;

        SphereSelection sphereSelection = null;
        if (selection instanceof  SphereSelection ss) {
            sphereSelection = ss;
        } else  return;
        double radius = sphereSelection.getRadius();
        XYZ center = sphereSelection.getCenter();

        Shape circle = Shape.createCircle(center.getX(), center.getZ(), radius, 80);
        ExtrudeMarker marker = new ExtrudeMarker.Builder()
                .shape(circle, (float) (center.getY() - (radius)), (float) (center.getY() + (radius)))
                .label(region.getName())
                .lineColor(lineColor)
                .lineWidth(3)
                .depthTestEnabled(false)
                .detail(infoWindow(region, selection)).build();
        set.getMarkers().put(regionId(region, selection, world), marker);
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