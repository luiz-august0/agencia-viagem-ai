package booking.service.dev.ia.outbound.tools;

import booking.service.dev.ia.core.domain.entity.Booking;
import booking.service.dev.ia.core.domain.enums.Category;
import booking.service.dev.ia.core.domain.usecase.CancelBookingUC;
import booking.service.dev.ia.core.domain.usecase.GetBookingDetailsUC;
import booking.service.dev.ia.core.domain.usecase.GetBookingsByCategoryUC;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
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

    @Tool(name = "Obtém os detalhes completos de uma reserva com base em seu número de identificação (bookingId).")
    public String getBookingDetails(@ToolArg(description = "ID da reserva a ser consultada") long bookingId) {
        return getBookingDetailsUC.execute(bookingId)
                .map(Booking::toString)
                .orElse("Reserva com ID " + bookingId + " não encontrada.");
    }

    @Tool(name = """
        Cancela uma reserva existente com base no seu ID (bookingId).
        O usuário deve estar autenticado.
    """)
    public String cancelBooking(@ToolArg(description = "ID da reserva a cancelar") long bookingId,
                                @ToolArg(description = "Nome do usuário") String name) {
        return cancelBookingUC.execute(bookingId, name)
                .map(b -> "Reserva " + b.id() + " cancelada com sucesso.")
                .orElse("Não foi possível cancelar a reserva. Verifique se o ID está correto e se você tem permissão.");
    }

    @Tool(name = "Lista os pacotes de viagem disponíveis para uma determinada categoria (ex: ADVENTURE, TREASURES).")
    public String listPackagesByCategory(@ToolArg(description = "Categoria dos pacotes a serem listados") Category category) {
        List<Booking> packages = getBookingsByCategoryUC.execute(category);
        if (packages.isEmpty()) {
            return "Nenhum pacote encontrado para a categoria: " + category;
        }
        return "Pacotes encontrados para a categoria '" + category + "': " + packages.stream()
                .map(Booking::destination)
                .toList();
    }

}