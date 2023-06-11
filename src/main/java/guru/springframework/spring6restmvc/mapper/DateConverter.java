package guru.springframework.spring6restmvc.mapper;

import org.mapstruct.Named;

import java.time.*;
import java.util.Objects;

/**
 * @deprecated (not used anymore)
 */
@Deprecated(forRemoval = true, since = "6/11/2023")
public interface DateConverter {

    @Named("mapCreatedToLocalDateTime")
    default LocalDateTime mapCreatedToLocalDateTime(OffsetDateTime createdDate) {
        ZonedDateTime zonedDateTime = Objects.requireNonNullElseGet(createdDate, OffsetDateTime::now)
            .atZoneSameInstant(ZoneId.of("UTC"));
        return zonedDateTime.toLocalDateTime();
    }

    @Named("mapUpdateToLocalDateTime")
    default LocalDateTime mapUpdateToLocalDateTime(OffsetDateTime updatedDate) {
        ZonedDateTime zonedDateTime = Objects.requireNonNullElseGet(updatedDate, OffsetDateTime::now)
            .atZoneSameInstant(ZoneId.of("UTC"));
        return zonedDateTime.toLocalDateTime();
    }

    @Named("mapCreatedToOffsetDateTime")
    default OffsetDateTime mapCreatedToOffsetDateTime(LocalDateTime createdDate) {
        final ZoneId zone = ZoneId.of("UTC");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(createdDate);
        return createdDate.atOffset(zoneOffSet);
    }

    @Named("mapUpdateToOffsetDateTime")
    default OffsetDateTime mapUpdateToOffsetDateTime(LocalDateTime updatedDate) {
        final ZoneId zone = ZoneId.of("UTC");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(updatedDate);
        return updatedDate.atOffset(zoneOffSet);
    }

}
