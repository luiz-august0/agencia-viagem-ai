package dev.ia.outbound.ai.tools;

import dev.ia.core.domain.entity.Booking;
import dev.ia.core.domain.enums.Category;
import dev.ia.core.domain.usecase.CancelBookingUC;
import dev.ia.core.domain.usecase.GetBookingDetailsUC;
import dev.ia.core.domain.usecase.GetBookingsByCategoryUC;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookingTools {

    @Inject
    GetBookingDetailsUC getBookingDetailsUC;

    @Inject
    CancelBookingUC cancelBookingUC;

    @Inject
    GetBookingsByCategoryUC getBookingsByCategoryUC;

    @Tool("Obtém os detalhes completos de uma reserva com base em seu número de identificação (bookingId).")
    public String getBookingDetails(long bookingId) {
        return getBookingDetailsUC.execute(bookingId)
                .map(Booking::toString)
                .orElse("Reserva com ID " + bookingId + " não encontrada.");
    }

    @Tool("""
        Cancela uma reserva existente com base no seu ID (bookingId).
        O usuário deve estar autenticado.
    """)
    public String cancelBooking(long bookingId) {
        return cancelBookingUC.execute(bookingId)
                .map(b -> "Reserva " + b.id() + " cancelada com sucesso.")
                .orElse("Não foi possível cancelar a reserva. Verifique se o ID está correto e se você tem permissão.");
    }

    @Tool("Lista os pacotes de viagem disponíveis para uma determinada categoria (ex: ADVENTURE, TREASURES).")
    public String listPackagesByCategory(Category category) {
        List<Booking> packages = getBookingsByCategoryUC.execute(category);
        if (packages.isEmpty()) {
            return "Nenhum pacote encontrado para a categoria: " + category;
        }
        return "Pacotes encontrados para a categoria '" + category + "': " + packages.stream()
                .map(Booking::destination)
                .toList();
    }

}