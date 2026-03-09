package booking.service.dev.ia.core.domain.usecase;

import booking.service.dev.ia.core.domain.entity.Booking;
import booking.service.dev.ia.core.domain.enums.Category;
import booking.service.dev.ia.core.gateway.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GetBookingsByCategoryUC {

    @Inject
    BookingRepository bookingRepository;

    public List<Booking> execute(Category category) {
        return this.bookingRepository.findAllByCategory(category);
    }

}
