package ru.practicum.explore.with.me.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Double latitude;
    private Double longitude;
}
