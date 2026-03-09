package booking.service.dev.ia.core.domain.entity;

import booking.service.dev.ia.core.domain.enums.BookingStatus;
import booking.service.dev.ia.core.domain.enums.Category;

import java.time.LocalDate;

public record Booking(
        Long id,
        String customerName,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status,
        Category category
) {}