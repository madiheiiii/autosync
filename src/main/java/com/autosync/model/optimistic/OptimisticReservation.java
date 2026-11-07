package com.autosync.model.optimistic;
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

@Entity(name  = "OptimisticReservation")
@Table(name = "reservation", schema = "optimistic")
public class OptimisticReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable=false)
    private OptimisticCustomer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private OptimisticVehicle vehicle;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptimisticReservationStatus status;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OptimisticReservation(){}

    public OptimisticReservation(OptimisticCustomer customer, OptimisticVehicle vehicle){
        this.customer = customer;
        this.vehicle = vehicle;
        this.status = OptimisticReservationStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = OptimisticReservationStatus.ACTIVE;
        }
    }

    public OptimisticCustomer getCustomer(){
        return customer;
    }

    public OptimisticVehicle getVehicle(){
        return vehicle;
    }

    public OptimisticReservationStatus getStatus(){
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCustomer(OptimisticCustomer customer){
        this.customer = customer;
    }

    public void setVehicle(OptimisticVehicle vehicle){
        this.vehicle = vehicle;
    }

    public void setStatus(OptimisticReservationStatus status) {
        this.status = status;
    }

    public Long getId(){
        return id;
    }
    
}
