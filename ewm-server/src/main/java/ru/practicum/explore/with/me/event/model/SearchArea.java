package ru.practicum.explore.with.me.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchArea {
    private Location location;
    private double radius;
}
