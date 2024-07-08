package bg.mck.sentinel.service;

import bg.mck.sentinel.entities.GoodSite;
import bg.mck.sentinel.reposotories.GoodSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodSiteService {

    private final GoodSiteRepository goodSiteRepository;

    @Autowired
    public GoodSiteService(GoodSiteRepository goodSiteRepository) {
        this.goodSiteRepository = goodSiteRepository;
    }


    public void save(GoodSite goodSite) {
        goodSiteRepository.save(goodSite);
    }

    public boolean isGoodSite(String domain) {
        return goodSiteRepository.existsByDomain(domain);
    }

    public void remove(String domain) {
        goodSiteRepository.findByDomain(domain).ifPresent(goodSiteRepository::delete);
    }

    public GoodSite findByDomain(String domain) {
        return goodSiteRepository.findByDomain(domain).orElse(null);
    }
}
