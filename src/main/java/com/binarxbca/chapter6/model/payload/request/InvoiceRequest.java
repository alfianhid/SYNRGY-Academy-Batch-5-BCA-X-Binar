package com.binarxbca.chapter6.model.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InvoiceRequest {
    @NotBlank
    @Schema(example = "14 Februari 2022")
    private String buyTicketDate;

    @NotBlank
    @Schema(example = "120000.0")
    private Double grandTotal;

    @NotBlank
    @Schema(example = "invoice.pdf")
    private String fileName;
}
