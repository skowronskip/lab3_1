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

    @Test
    public void shouldReturnOneInvoiceWithProperClientDataWhenInvoiceRequestContainsOneItem() {
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(new Money(new BigDecimal(100)), "Description"));
        ClientData clientData = new ClientData(new Id("id"), "Test client");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        ProductData productData = Mockito.mock(ProductData.class);
        Mockito.when(productData.getType())
                .thenReturn(ProductType.STANDARD);
        invoiceRequest.add(new RequestItem(productData, 1, new Money(new BigDecimal(100))));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(clientData.getName(), invoice.getClient().getName());
        assertEquals(clientData.getAggregateId().getId(), invoice.getClient().getAggregateId().getId());
    }

    @Test
    public void shouldReturnAllInvoiceWithProperNetAndGrosAmounts() {
        BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
        TaxPolicy taxPolicy = Mockito.mock(TaxPolicy.class);
        Mockito.when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(new Money(new BigDecimal(100)), "Description"));
        ClientData clientData = new ClientData(new Id("id"), "Test client");
        InvoiceRequest invoiceRequest = new InvoiceRequest(clientData);
        ProductData productData = Mockito.mock(ProductData.class);
        Mockito.when(productData.getType())
                .thenReturn(ProductType.STANDARD);
        invoiceRequest.add(new RequestItem(productData, 1, new Money(new BigDecimal(100))));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(new Money(new BigDecimal(200)), invoice.getGros());
        assertEquals(new Money(new BigDecimal(100)), invoice.getNet());
    }

    @Test
    public void shouldExecuteGetTypeTwice() {
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

        Mockito.verify(productData, Mockito.times(2)).getType();
    }

    @Test
    public void shouldUseInvoiceFactoryOnce() {
        InvoiceFactory invoiceFactory = Mockito.mock(InvoiceFactory.class);
        Mockito.when(invoiceFactory.create(any(ClientData.class))).thenReturn(new Invoice(new Id("ID"), new ClientData(new Id("ID"), "Test client")));
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
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

        Mockito.verify(invoiceFactory, Mockito.times(1)).create(any(ClientData.class));
    }
}