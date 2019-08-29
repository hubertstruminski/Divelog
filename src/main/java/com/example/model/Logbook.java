package com.example.model;

import com.example.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "logbook")
public class Logbook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "partner_name")
    @Size(min = 1, max = 255, message = "Length should be 1 to 255 characters")
    private String partnerName;

    @Column(name = "partner_surname")
    @Size(min = 1, max = 255, message = "Length should be 1 to 255 characters")
    private String partnerSurname;

    @ManyToOne
    @JoinColumn(name = "marker_id")
    @NotBlank(message = "You have to set marker")
    @NotNull
    private Marker marker;

    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm")
    @Column(name = "entry_time")
    private Date entryTime;

    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm")
    @Column(name = "exit_time")
    private Date exitTime;

    @Column(name = "average_depth")
    private double averageDepth;

    @Column(name = "max_depth")
    private double maxDepth;

    @Column(name = "visibility")
    private double visibility;

    @Column(name = "cylinder_capacity")
    @NotBlank(message = "You have to set cylinder capacity")
    private String cylinderCapacity; // 10L, 12L, 15L, 18L, inny

    @Column(name = "diving_suit")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "You have to set diving suit")
    private DivingSuit divingSuit; // Pianka, suchy, półsuchy

    @Column(name = "water_type")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "You have to set type of water")
    @NotNull
    private WaterType waterType; // woda slona, woda slodka

    @Column(name = "type_water_entry")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "You have to set type of entry to water")
    @NotNull
    private WaterEntryType waterEntryType; // z brzegu, z lodki

    @Column(name = "ballast")
    private double ballast;

    @Column(name = "gloves_type")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "You have to set type of gloves")
    @NotNull
    private GloveType glovesType;

    @Column(name = "diving_type")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "You have to set type of diving")
    private DivingType divingType;

    @Column(name = "comment")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
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
