package ru.practicum.explore.with.me.event.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.explore.with.me.event.model.Location;

// This class also is used for updating by admin (but without validation)

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EventCreateDto extends BaseEventUpdateDto {
    private Location location;
    private Boolean requestModeration;

    public void setDefaultsForNew() {
        if (getPaid() == null) {
            setPaid(false);
        }
        if (getParticipantLimit() == null) {
            setParticipantLimit(0);
        }
        if (getRequestModeration() == null) {
            setRequestModeration(true);
        }
    }
}
