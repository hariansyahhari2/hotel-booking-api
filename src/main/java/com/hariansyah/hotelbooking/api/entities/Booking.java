package com.hariansyah.hotelbooking.api.entities;

import com.hariansyah.hotelbooking.api.enums.StatusEnum;

import javax.persistence.*;
import java.time.LocalDate;

import static com.hariansyah.hotelbooking.api.enums.StatusEnum.CONFIRMED;

@Table
@Entity(name = "booking")
public class Booking extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "person_count")
    private Integer personCount;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private Account bookedBy;

    @ManyToOne
    @JoinColumn(name = "guest")
    private CustomerIdentity guest;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "number_of_night")
    private Long numberOfNight;

    @Column(name = "room_count")
    private Long roomCount;

    @Column
    private Double subTotal;

    @Enumerated
    @Column
    private StatusEnum status = CONFIRMED;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Account getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(Account bookedBy) {
        this.bookedBy = bookedBy;
    }

    public CustomerIdentity getGuest() {
        return guest;
    }

    public void setGuest(CustomerIdentity guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getNumberOfNight() {
        return numberOfNight;
    }

    public void setNumberOfNight(Long numberOfNight) {
        this.numberOfNight = numberOfNight;
    }

    public Long getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Long roomCount) {
        this.roomCount = roomCount;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
