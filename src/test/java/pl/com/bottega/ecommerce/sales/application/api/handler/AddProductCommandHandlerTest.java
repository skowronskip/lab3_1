package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.junit.Test;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

public class AddProductCommandHandlerTest {

    @Test
    public void shouldReturnNull() {
        ReservationRepository reservationRepository = Mockito.mock(ReservationRepository.class);
        Reservation reservation = Mockito.mock(Reservation.class);
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        SuggestionService suggestionService = Mockito.mock(SuggestionService.class);
        ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
        SystemContext systemContext = Mockito.mock(SystemContext.class);
        Product product = Mockito.mock(Product.class);
        Mockito.when(reservationRepository.load(any(Id.class))).thenReturn(reservation);
        Mockito.when(productRepository.load(any(Id.class))).thenReturn(product);
        Mockito.when(product.isAvailable()).thenReturn(true);
        Mockito.doNothing().when(reservation).add(any(Product.class), any(int.class));
        Mockito.doNothing().when(reservationRepository).save(any(Reservation.class));

        AddProductCommandHandler addProductCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, suggestionService, clientRepository, systemContext);
        assertNull(addProductCommandHandler.handle(new AddProductCommand(Id.generate(), Id.generate(), 1)));
    }
}