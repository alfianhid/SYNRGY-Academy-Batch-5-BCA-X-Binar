package com.binarxbca.chapter6.service.impl;

import com.binarxbca.chapter6.model.InvoiceDetail;
import com.binarxbca.chapter6.repository.InvoiceDetailRepository;
import com.binarxbca.chapter6.service.InvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceDetailServiceImpl implements InvoiceDetailService {
    @Autowired
    InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public InvoiceDetail saveInvoiceDetail(InvoiceDetail invoiceDetail) {
        return invoiceDetailRepository.save(invoiceDetail);
    }
}
