package com.binarxbca.chapter6.controller;

import com.binarxbca.chapter6.model.Invoice;
import com.binarxbca.chapter6.model.payload.request.InvoiceRequest;
import com.binarxbca.chapter6.service.InvoiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/chapter6/auth/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add")
    public ResponseEntity<Invoice> addInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        return invoiceService.addInvoice(invoiceRequest);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/buy_ticket")
    public String saveTransaction(@RequestBody Invoice invoice) throws JsonProcessingException {
        invoiceService.doTransaction(invoice);

        return "Transaction has been made. Thank you!";
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/generate")
    public void generateInvoice(HttpServletResponse httpServletResponse,
                                  @RequestParam(value = "id") Long id) {
        invoiceService.generateInvoice(httpServletResponse, id);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/download")
    public ResponseEntity downloadFile(@RequestParam("id") Long id) {
        Invoice invoice = invoiceService.downloadInvoice(id);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok().headers(headers).body(invoice.getFile());
    }
}
