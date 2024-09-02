package bg.mck.sentinel.service;

import bg.mck.sentinel.entities.PenalizedUser;
import bg.mck.sentinel.reposotories.PenalizedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PunishmentService {

    private final PenalizedUserRepository penalizedUserRepository;

    @Autowired
    public PunishmentService(PenalizedUserRepository penalizedUserRepository) {
        this.penalizedUserRepository = penalizedUserRepository;
    }

    public PenalizedUser findByUserId(String id) {
        return this.penalizedUserRepository.findByUserId(id).orElse(null);
    }

    public void save(PenalizedUser user) {
        this.penalizedUserRepository.save(user);
    }
}
