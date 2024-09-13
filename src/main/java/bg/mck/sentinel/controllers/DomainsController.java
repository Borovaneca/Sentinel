package bg.mck.sentinel.controllers;

import bg.mck.sentinel.reposotories.GoodSiteRepository;
import bg.mck.sentinel.reposotories.SubDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/domains")
public class DomainsController {

    private final GoodSiteRepository goodSiteRepository;
    private final SubDomainRepository subDomainRepository;

    @Autowired
    public DomainsController(GoodSiteRepository goodSiteRepository, SubDomainRepository subDomainRepository) {
        this.goodSiteRepository = goodSiteRepository;
        this.subDomainRepository = subDomainRepository;
    }

    @GetMapping("/domain")
    public ResponseEntity<?> getDomains() {
        return ResponseEntity.ok().body(goodSiteRepository.findAll());
    }

    @GetMapping("/subdomains")
    public ResponseEntity<?> getSubDomain() {
        return ResponseEntity.ok().body(subDomainRepository.findAll());
    }
}
