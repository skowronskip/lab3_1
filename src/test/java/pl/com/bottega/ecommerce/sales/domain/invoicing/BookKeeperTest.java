package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

public class BookKeeperTest {

    @Test
    public void shouldReturnOneInvoiceWhenInvoiceRequestContainsOneItem() {
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(new Money(new BigDecimal(100)), "Description"));
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(new Id("id"), "test client"));
        ProductData productData = Mockito.mock(ProductData.class);
        Mockito.when(productData.getType())
                .thenReturn(ProductType.STANDARD);
        invoiceRequest.add(new RequestItem(productData, 1, new Money(new BigDecimal(100))));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(1, invoice.getItems().size());
    }

    @Test
    public void shouldExecuteCalculateTaxTwice() {
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(new Money(new BigDecimal(100)), "Description"));
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(new Id("id"), "test client"));
        ProductData productData = Mockito.mock(ProductData.class);
        Mockito.when(productData.getType())
                .thenReturn(ProductType.STANDARD);
        invoiceRequest.add(new RequestItem(productData, 1, new Money(new BigDecimal(100))));
        invoiceRequest.add(new RequestItem(productData, 1, new Money(new BigDecimal(100))));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }
}