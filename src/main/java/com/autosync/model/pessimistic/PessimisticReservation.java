package com.autosync.model.pessimistic;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity(name = "PessimisticReservation")
@Table(name = "reservation", schema = "pessimistic")
public class PessimisticReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private PessimisticCustomer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private PessimisticVehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PessimisticReservationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PessimisticReservation() {
    }

    public PessimisticReservation(PessimisticCustomer customer, PessimisticVehicle vehicle) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.status = PessimisticReservationStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = PessimisticReservationStatus.ACTIVE;
        }
    }

    public PessimisticCustomer getCustomer() {
        return customer;
    }

    public PessimisticVehicle getVehicle() {
        return vehicle;
    }

    public PessimisticReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCustomer(PessimisticCustomer customer) {
        this.customer = customer;
    }

    public void setVehicle(PessimisticVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setStatus(PessimisticReservationStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

}
