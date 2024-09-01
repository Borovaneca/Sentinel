package bg.mck.sentinel.service;

import bg.mck.sentinel.entities.SubDomain;
import bg.mck.sentinel.reposotories.SubDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SubDomainService {

    private final SubDomainRepository subDomainRepository;

    @Autowired
    public SubDomainService(SubDomainRepository subDomainRepository) {
        this.subDomainRepository = subDomainRepository;
    }

    public void saveSubDomain(SubDomain subDomain) {
        subDomainRepository.save(subDomain);
    }


    public String findBySubDomain(String subDomain) {
        SubDomain bySubDomain = subDomainRepository.getBySubDomain(subDomain);
        if (bySubDomain == null) {
            return null;
        }
        return bySubDomain.getSubDomain();
    }

    public String getAllSubDomainsForRegex() {
        if (subDomainRepository.findAll().isEmpty()) {
            return null;
        }
        return subDomainRepository.findAll().stream().map(SubDomain::getSubDomain).collect(Collectors.joining("|"));
    }

    public String getAllSubDomains() {
        if (subDomainRepository.findAll().isEmpty()) {
            return "No subdomains found!";
        }
        return subDomainRepository.findAll().stream().map(SubDomain::getSubDomain).collect(Collectors.joining("\n"));
    }
}
