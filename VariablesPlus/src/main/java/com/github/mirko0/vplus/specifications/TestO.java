package com.github.mirko0.vplus.specifications;

public class TestO {

    private String name;
    private String location;
    private String time;

    public TestO(String name, String location, String time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String serialize() {
        return name + "," + location + "," + time;
    }

    public static TestO deserialize(String data) {
        String[] split = data.split(",");
        return new TestO(split[0], split[1], split[2]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
