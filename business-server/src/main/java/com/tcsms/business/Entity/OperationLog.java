package com.tcsms.business.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "operation_log")
public class OperationLog implements Serializable {
    @Id    //主键id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")//数据库字段名
    private Integer id;
    @Column(name = "deviceModel")//数据库字段名
    private String deviceModel;
    @Column(name = "deviceId")//数据库字段名
    private String deviceId;
    @Column(name = "operator")//数据库字段名
    private String operator;
    @Column(name = "workerId")//数据库字段名
    private String workerId;
    @Column(name = "time")//数据库字段名
    private String time;
    @Column(name = "longitude")//数据库字段名
    private Double longitude;
    @Column(name = "latitude")//数据库字段名
    private Double latitude;
    @Column(name = "radius")//数据库字段名
    private Double radius;
    @Column(name = "angle")//数据库字段名
    private Double angle;
    @Column(name = "height")//数据库字段名
    private Double height;
    @Column(name = "torque")//数据库字段名
    private Double torque;
    @Column(name = "weight")//数据库字段名
    private Double weight;
    @Column(name = "windVelocity")//数据库字段名
    private Double windVelocity;
    @Column(name = "magnification")//数据库字段名
    private int magnification;

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceID() {
        return deviceId;
    }

    public void setDeviceID(String deviceID) {
        this.deviceId = deviceID;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getTorque() {
        return torque;
    }

    public void setTorque(Double torque) {
        this.torque = torque;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWindVelocity() {
        return windVelocity;
    }

    public void setWindVelocity(Double windVelocity) {
        this.windVelocity = windVelocity;
    }

    public int getMagnification() {
        return magnification;
    }

    public void setMagnification(int magnification) {
        this.magnification = magnification;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
        return "{" +
                "\"deviceModel\":" + "\"" + deviceModel + "\"" + "," +
                "\"deviceId\":" + "\"" + deviceId + "\"" + "," +
                "\"operator\":" + "\"" + operator + "\"" + "," +
                "\"workerId\":" + "\"" + workerId + "\"" + "," +
                "\"time\":" + "\"" + time + "\"" + "," +
                "\"longitude\":" + longitude + "," +
                "\"latitude\":" + latitude + "," +
                "\"radius\":" + radius + "," +
                "\"angle\":" + angle + "," +
                "\"height\":" + height + "," +
                "\"torque\":" + torque + "," +
                "\"weight\":" + weight + "," +
                "\"windVelocity\":" + windVelocity + "," +
                "\"magnification\":" + magnification +
                "}";
    }
}
