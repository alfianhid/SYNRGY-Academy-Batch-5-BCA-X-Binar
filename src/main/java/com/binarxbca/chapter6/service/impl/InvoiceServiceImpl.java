package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.exception.BadRequestException;
import com.binarxbca.chapter6.exception.ResourceNotFoundException;
import com.binarxbca.chapter6.model.Film;
import com.binarxbca.chapter6.model.Invoice;
import com.binarxbca.chapter6.model.InvoiceDetail;
import com.binarxbca.chapter6.model.User;
import com.binarxbca.chapter6.model.payload.request.InvoiceRequest;
import com.binarxbca.chapter6.model.payload.response.ApiResponse;
import com.binarxbca.chapter6.repository.InvoiceRepository;
import com.binarxbca.chapter6.service.FilmService;
import com.binarxbca.chapter6.service.InvoiceDetailService;
import com.binarxbca.chapter6.service.InvoiceService;
import com.binarxbca.chapter6.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;

import java.util.*;

import static com.binarxbca.chapter6.utils.AppConstants.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Invoice> addInvoice(InvoiceRequest invoiceRequest) {
        Invoice invoice = new Invoice();

        modelMapper.map(invoiceRequest, invoice);

        Invoice invoice1 = invoiceRepository.save(invoice);
        return new ResponseEntity<>(invoice1, HttpStatus.CREATED);
    }

    @Override
    public void doTransaction(Invoice invoice) throws JsonProcessingException {
        Invoice invoices = invoiceRepository.save(invoice);

        User user = userService.getUserById(invoice.getUser().getId());
        invoices.setUser(user);

        for (InvoiceDetail invoiceDetail : invoices.getInvoiceDetailList()) {
            invoiceDetail.setInvoice(invoices);
            Film film = filmService.getFilmById(invoiceDetail.getFilm().getId());

            Double grandTotal = 0.0;

            if (film.getStock() == 0) {
                ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Film is out of stock");
                throw new BadRequestException(apiResponse);
            }
            if (film.getStock() < invoiceDetail.getQuantity()) {
                String message = String.format(BAD_REQUEST, film.getStock(), invoiceDetail.getQuantity(), film.getTitle());
                throw new ResourceNotFoundException(message);
            }

            film.setStock(film.getStock() - invoiceDetail.getQuantity());

            invoiceDetail.setPrice(film.getTicketPrice() * invoiceDetail.getQuantity());
            invoiceDetail.setFilm(film);
            invoiceDetailService.saveInvoiceDetail(invoiceDetail);

            grandTotal += invoiceDetail.getPrice();
            invoices.setGrandTotal(grandTotal);
        }

        String response = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(invoices);
        System.out.println(response);
    }

    @Override
    public void generateInvoice(HttpServletResponse response, Long id) {
        try {
            JasperReport sourceFileName = JasperCompileManager.compileReport
                    (ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX
                            + "generate_invoice.jrxml").getAbsolutePath());

            // creating our list of beans
            List<Map<String,String>> dataInvoice = new ArrayList<>();
            Map<String, String> invoice = new HashMap<>();
            Invoice invoices = invoiceRepository.findById(id).get();

            invoice.put("id", String.valueOf(invoices.getId()));
            invoice.put("buyTicketDate", invoices.getBuyTicketDate());
            invoice.put("user_id", String.valueOf(invoices.getUser().getId()));
            invoice.put("grandTotal", String.valueOf(invoices.getGrandTotal()));

            dataInvoice.add(invoice);

            // creating datasource from bean list
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataInvoice);
            Map<String,Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);

            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=generate_invoice.pdf;");

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Invoice downloadInvoice(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }


}
