package com.sparta.domain.store.entity;

import com.sparta.domain.user.entity.User;
import com.sparta.global.entity.TimeStamped;
import com.sparta.global.exception.customException.StoreException;
import com.sparta.global.exception.errorCode.StoreErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String workHours;

    private String storeImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreRegion storeRegion;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Builder
    public Store(String name, String address, String phoneNumber, String workHours, String storeImage, User manager, StoreRegion storeRegion, StoreStatus storeStatus) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.workHours = workHours;
        this.storeImage = storeImage;
        this.manager = manager;
        this.storeRegion = storeRegion;
        this.storeStatus = storeStatus;
    }

    public void updateStore(String name, String address, String phoneNumber, String workHours, StoreRegion storeRegion) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.workHours = workHours;
        this.storeRegion = storeRegion;
    }

    public void updateStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public void deleteStoreImage() {
        this.storeImage = null;
    }

    public void deactivateStore() {
        this.storeStatus = StoreStatus.INACTIVE;
    }

    public void activateStore() {
        this.storeStatus = StoreStatus.ACTIVE;
    }

    public void checkManager(User manager) {
        if(!Objects.equals(this.manager.getId(), manager.getId())) {
            throw new StoreException(StoreErrorCode.USER_NOT_STORE_MANAGER);
        }
    }

    public void verifyStoreIsActive() {
        if(this.storeStatus != StoreStatus.ACTIVE) {
            throw new StoreException(StoreErrorCode.INVALID_STORE_STATUS);
        }
    }
}
