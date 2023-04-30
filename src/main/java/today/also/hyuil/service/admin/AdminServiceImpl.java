package today.also.hyuil.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Admin;
import today.also.hyuil.repository.admin.AdminRepository;
import today.also.hyuil.service.admin.inter.AdminService;

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
