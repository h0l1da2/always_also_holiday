package today.also.hyuil.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.member.domain.Admin;
import today.also.hyuil.member.repository.AdminRepository;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin accountAdmin(String adminId) {
        return adminRepository.findByAdminId(adminId);
    }
}
