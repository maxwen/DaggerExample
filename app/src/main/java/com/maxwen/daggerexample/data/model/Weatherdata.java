package com.maxwen.daggerexample.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Root(name = "weatherdata")
public class Weatherdata {

    static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Element(name = "product", required = false)
    Product product;

    @Attribute(name = "created", required = false)
    String created;

    @Element(name = "meta", required = false)
    Meta meta;

    @Attribute(name = "noNamespaceSchemaLocation", required = false)
    URL noNamespaceSchemaLocation;

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product value) {
        this.product = value;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String value) {
        this.created = value;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Meta value) {
        this.meta = value;
    }

    public URL getNoNamespaceSchemaLocation() {
        return this.noNamespaceSchemaLocation;
    }

    public void setNoNamespaceSchemaLocation(URL value) {
        this.noNamespaceSchemaLocation = value;
    }

    public static class Symbol {

        @Attribute(name = "number", required = false)
        Integer number;

        @Attribute(name = "id", required = false)
        String id;

        public Integer getNumber() {
            return this.number;
        }

        public void setNumber(Integer value) {
            this.number = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

    }

    public static class Product {

        @ElementList(name = "time", required = false, entry = "time", inline = true)
        List<Time> time;

        @Attribute(name = "class", required = false)
        String _class;

        public List<Time> getTime() {
            return this.time;
        }

        public void setTime(List<Time> value) {
            this.time = value;
        }

        public String get_class() {
            return this._class;
        }

        public void set_class(String value) {
            this._class = value;
        }

    }

    public static class HighClouds {

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "percent", required = false)
        Double percent;

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getPercent() {
            return this.percent;
        }

        public void setPercent(Double value) {
            this.percent = value;
        }

    }

    public static class Pressure {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class Cloudiness {

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "percent", required = false)
        Double percent;

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getPercent() {
            return this.percent;
        }

        public void setPercent(Double value) {
            this.percent = value;
        }

    }

    public static class Precipitation {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class MaxTemperature {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class MinTemperature {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class LowClouds {

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "percent", required = false)
        Double percent;

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getPercent() {
            return this.percent;
        }

        public void setPercent(Double value) {
            this.percent = value;
        }

    }

    public static class Meta {

        @Element(name = "model", required = false)
        Model model;

        public Model getModel() {
            return this.model;
        }

        public void setModel(Model value) {
            this.model = value;
        }

    }

    public static class Temperature {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class MediumClouds {

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "percent", required = false)
        Double percent;

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getPercent() {
            return this.percent;
        }

        public void setPercent(Double value) {
            this.percent = value;
        }

    }

    public static class Humidity {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class Model {

        @Attribute(name = "nextrun", required = false)
        String nextrun;

        @Attribute(name = "termin", required = false)
        String termin;

        @Attribute(name = "name", required = false)
        String name;

        @Attribute(name = "runended", required = false)
        String runended;

        @Attribute(name = "from", required = false)
        String from;

        @Attribute(name = "to", required = false)
        String to;

        // 2019-08-02T00:00:00Z
        // 2019-08-02T01:00:00Z


        public String getNextrun() {
            return this.nextrun;
        }

        public void setNextrun(String value) {
            this.nextrun = value;
        }

        public String getTermin() {
            return this.termin;
        }

        public void setTermin(String value) {
            this.termin = value;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public String getRunended() {
            return this.runended;
        }

        public void setRunended(String value) {
            this.runended = value;
        }

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String value) {
            this.from = value;
        }

        public String getTo() {
            return this.to;
        }

        public void setTo(String value) {
            this.to = value;
        }

    }

    public static class Location {

        @Attribute(name = "altitude", required = false)
        String altitude;

        @Element(name = "symbol", required = false)
        Symbol symbol;

        @Element(name = "highClouds", required = false)
        HighClouds highClouds;

        @Attribute(name = "latitude", required = false)
        String latitude;

        @Element(name = "pressure", required = false)
        Pressure pressure;

        @Element(name = "cloudiness", required = false)
        Cloudiness cloudiness;

        @Element(name = "precipitation", required = false)
        Precipitation precipitation;

        @Element(name = "minTemperature", required = false)
        MinTemperature minTemperature;

        @Element(name = "maxTemperature", required = false)
        MaxTemperature maxTemperature;

        @Element(name = "lowClouds", required = false)
        LowClouds lowClouds;

        @Element(name = "temperature", required = false)
        Temperature temperature;

        @Element(name = "mediumClouds", required = false)
        MediumClouds mediumClouds;

        @Element(name = "humidity", required = false)
        Humidity humidity;

        @Element(name = "dewpointTemperature", required = false)
        DewpointTemperature dewpointTemperature;

        @Element(name = "windDirection", required = false)
        WindDirection windDirection;

        @Element(name = "windSpeed", required = false)
        WindSpeed windSpeed;

        @Attribute(name = "longitude", required = false)
        String longitude;

        @Element(name = "fog", required = false)
        Fog fog;

        public String getAltitude() {
            return this.altitude;
        }

        public void setAltitude(String value) {
            this.altitude = value;
        }

        public Symbol getSymbol() {
            return this.symbol;
        }

        public void setSymbol(Symbol value) {
            this.symbol = value;
        }

        public HighClouds getHighClouds() {
            return this.highClouds;
        }

        public void setHighClouds(HighClouds value) {
            this.highClouds = value;
        }

        public String getLatitude() {
            return this.latitude;
        }

        public void setLatitude(String value) {
            this.latitude = value;
        }

        public Pressure getPressure() {
            return this.pressure;
        }

        public void setPressure(Pressure value) {
            this.pressure = value;
        }

        public Cloudiness getCloudiness() {
            return this.cloudiness;
        }

        public void setCloudiness(Cloudiness value) {
            this.cloudiness = value;
        }

        public Precipitation getPrecipitation() {
            return this.precipitation;
        }

        public void setPrecipitation(Precipitation value) {
            this.precipitation = value;
        }

        public MinTemperature getMinTemperature() {
            return this.minTemperature;
        }

        public void setMinTemperature(MinTemperature value) {
            this.minTemperature = value;
        }

        public MaxTemperature getMaxTemperature() {
            return this.maxTemperature;
        }

        public void setMaxTemperature(MaxTemperature value) {
            this.maxTemperature = value;
        }

        public LowClouds getLowClouds() {
            return this.lowClouds;
        }

        public void setLowClouds(LowClouds value) {
            this.lowClouds = value;
        }

        public Temperature getTemperature() {
            return this.temperature;
        }

        public void setTemperature(Temperature value) {
            this.temperature = value;
        }

        public MediumClouds getMediumClouds() {
            return this.mediumClouds;
        }

        public void setMediumClouds(MediumClouds value) {
            this.mediumClouds = value;
        }

        public Humidity getHumidity() {
            return this.humidity;
        }

        public void setHumidity(Humidity value) {
            this.humidity = value;
        }

        public DewpointTemperature getDewpointTemperature() {
            return this.dewpointTemperature;
        }

        public void setDewpointTemperature(DewpointTemperature value) {
            this.dewpointTemperature = value;
        }

        public WindDirection getWindDirection() {
            return this.windDirection;
        }

        public void setWindDirection(WindDirection value) {
            this.windDirection = value;
        }

        public WindSpeed getWindSpeed() {
            return this.windSpeed;
        }

        public void setWindSpeed(WindSpeed value) {
            this.windSpeed = value;
        }

        public String getLongitude() {
            return this.longitude;
        }

        public void setLongitude(String value) {
            this.longitude = value;
        }

        public Fog getFog() {
            return this.fog;
        }

        public void setFog(Fog value) {
            this.fog = value;
        }

    }

    public static class DewpointTemperature {

        @Attribute(name = "unit", required = false)
        String unit;

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "value", required = false)
        Double value;

        public String getUnit() {
            return this.unit;
        }

        public void setUnit(String value) {
            this.unit = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getValue() {
            return this.value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    }

    public static class Time {

        @Attribute(name = "datatype", required = false)
        String datatype;

        @Attribute(name = "from", required = false)
        String from;

        @Element(name = "location", required = false)
        Location location;

        @Attribute(name = "to", required = false)
        String to;

        public String getDatatype() {
            return this.datatype;
        }

        public void setDatatype(String value) {
            this.datatype = value;
        }

        public String getFrom() {
            return this.from;
        }

        public Date getFromDate() throws ParseException {
            Date date = timeFormat.parse(this.from);
            return date;
        }

        public void setFrom(String value) {
            this.from = value;
        }

        public Location getLocation() {
            return this.location;
        }

        public void setLocation(Location value) {
            this.location = value;
        }

        public String getTo() {
            return this.to;
        }

        public Date getToDate() throws ParseException {
            Date date = timeFormat.parse(this.to);
            return date;
        }

        public void setTo(String value) {
            this.to = value;
        }

    }

    public static class WindDirection {

        @Attribute(name = "deg", required = false)
        Double deg;

        @Attribute(name = "name", required = false)
        String name;

        @Attribute(name = "id", required = false)
        String id;

        public Double getDeg() {
            return this.deg;
        }

        public void setDeg(Double value) {
            this.deg = value;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

    }

    public static class WindSpeed {

        @Attribute(name = "mps", required = false)
        Double mps;

        @Attribute(name = "name", required = false)
        String name;

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "beaufort", required = false)
        Integer beaufort;

        public Double getMps() {
            return this.mps;
        }

        public void setMps(Double value) {
            this.mps = value;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Integer getBeaufort() {
            return this.beaufort;
        }

        public void setBeaufort(Integer value) {
            this.beaufort = value;
        }

    }

    public static class Fog {

        @Attribute(name = "id", required = false)
        String id;

        @Attribute(name = "percent", required = false)
        Double percent;

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public Double getPercent() {
            return this.percent;
        }

        public void setPercent(Double value) {
            this.percent = value;
        }

    }

}