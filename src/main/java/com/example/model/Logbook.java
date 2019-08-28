package com.example.model;

import com.example.enums.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "logbook")
public class Logbook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "partner_surname")
    private String partnerSurname;

    @ManyToOne
    @JoinColumn(name = "marker_id")
    @NotNull
    private Marker marker;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @Temporal(TemporalType.TIME)
    @Column(name = "entry_time")
    private Date entryTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "exit_time")
    private Date exitTime;

    @Column(name = "average_depth")
    private double averageDepth;

    @Column(name = "max_depth")
    private double maxDepth;

    @Column(name = "visibility")
    private double visibility;

    @Column(name = "cylinder_capacity")
    private String cylinderCapacity; // 10L, 12L, 15L, 18L, inny

    @Column(name = "diving_suit")
    private DivingSuit divingSuit; // Pianka, suchy, półsuchy

    @Column(name = "water_type")
    @NotNull
    private WaterType waterType; // woda slona, woda slodka

    @Column(name = "type_water_entry")
    @NotNull
    private WaterEntryType waterEntryType; // z brzegu, z lodki

    @Column(name = "ballast")
    @NotNull
    private double ballast;

    @Column(name = "gloves_type")
    @NotNull
    private GloveType glovesType;

    @Column(name = "diving_type")
    private DivingType divingType;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Connection user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerSurname() {
        return partnerSurname;
    }

    public void setPartnerSurname(String partnerSurname) {
        this.partnerSurname = partnerSurname;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public double getAverageDepth() {
        return averageDepth;
    }

    public void setAverageDepth(double averageDepth) {
        this.averageDepth = averageDepth;
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public String getCylinderCapacity() {
        return cylinderCapacity;
    }

    public void setCylinderCapacity(String cylinderCapacity) {
        this.cylinderCapacity = cylinderCapacity;
    }

    public DivingSuit getDivingSuit() {
        return divingSuit;
    }

    public void setDivingSuit(DivingSuit divingSuit) {
        this.divingSuit = divingSuit;
    }

    public WaterType getWaterType() {
        return waterType;
    }

    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }

    public WaterEntryType getWaterEntryType() {
        return waterEntryType;
    }

    public void setWaterEntryType(WaterEntryType waterEntryType) {
        this.waterEntryType = waterEntryType;
    }

    public double getBallast() {
        return ballast;
    }

    public void setBallast(double ballast) {
        this.ballast = ballast;
    }

    public GloveType getGlovesType() {
        return glovesType;
    }

    public void setGlovesType(GloveType glovesType) {
        this.glovesType = glovesType;
    }

    public DivingType getDivingType() {
        return divingType;
    }

    public void setDivingType(DivingType divingType) {
        this.divingType = divingType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Connection getUser() {
        return user;
    }

    public void setUser(Connection user) {
        this.user = user;
    }
}
