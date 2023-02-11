package com.binarxbca.chapter6.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class SeatId implements Serializable {
    private String seatRow;
    private String seatColumn;
}