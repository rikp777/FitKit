package nl.rikp.customerService.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.service.CoinService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoinServiceImpl extends CustomerBaseService implements CoinService {

    public CoinServiceImpl(CustomerRepository customerRepo, CustomerMapper customerMapper) {
        super(customerRepo, customerMapper);
    }

}
