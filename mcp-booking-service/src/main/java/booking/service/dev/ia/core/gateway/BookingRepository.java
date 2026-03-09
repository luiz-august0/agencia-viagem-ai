package booking.service.dev.ia.core.gateway;

import booking.service.dev.ia.core.domain.entity.Booking;
import booking.service.dev.ia.core.domain.enums.Category;

import java.util.List;

public interface BookingRepository {

    Booking findById(Long bookingId);

    Booking update(Long bookingId, Booking booking);

    boolean hasBooking(Long bookingId);

    List<Booking> findAllByCategory(Category category);

}
